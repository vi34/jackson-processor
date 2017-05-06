package com.vshatrov;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.*;
import com.vshatrov.beans.BeanDescription;
import com.vshatrov.beans.Inspector;
import com.vshatrov.generation.*;
import com.vshatrov.utils.Utils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import static com.vshatrov.utils.Utils.*;

/**
 * @author Viktor Shatrov.
 */
@AutoService(javax.annotation.processing.Processor.class)
@SupportedAnnotationTypes({
        "com.vshatrov.annotations.*",
        JacksonProcessor.JSON_SERIALIZE,
        JacksonProcessor.JSON_DESERIALIZE
})
public class JacksonProcessor extends AbstractProcessor {

    private static final boolean DEBUG = true;
    public static final String DEFAULT_SCHEMA_DIR = "./json-schema";
    public static final String JSON_DESERIALIZE = "com.fasterxml.jackson.databind.annotation.JsonDeserialize";
    public static final String JSON_SERIALIZE = "com.fasterxml.jackson.databind.annotation.JsonSerialize";
    private Map<String, SerializationInfo> processedSerializers;
    private Map<String, DeserializationInfo> processedDeserializers;

    private boolean autoRegistration;
    private String schema_dir;


    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        JavacProcessingEnvironment javacEnv = (JavacProcessingEnvironment) env;
        Utils.typeUtils = processingEnv.getTypeUtils();
        Utils.elementUtils = processingEnv.getElementUtils();
        Utils.filer = processingEnv.getFiler();
        Utils.messager = processingEnv.getMessager();
        Utils.trees = Trees.instance(env);
        Utils.treeMaker = TreeMaker.instance(javacEnv.getContext());
        Utils.names = Names.instance(javacEnv.getContext());
        Utils.javacElements = javacEnv.getElementUtils();

        processedSerializers = new HashMap<>();
        processedDeserializers = new HashMap<>();
        Inspector.beansCache = new HashMap<>();
        fetchOptions();
    }

    private void fetchOptions() {
        Map<String, String> options = processingEnv.getOptions();
        schema_dir = options.getOrDefault("SCHEMA_DIR", DEFAULT_SCHEMA_DIR);
        autoRegistration = Boolean.parseBoolean(options.getOrDefault("AUTO_REGISTRATION", "true"));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            List<String> sourceTypes = new ArrayList<>();
            Stream.concat(roundEnv.getElementsAnnotatedWith(JsonSerialize.class).stream(),
                    roundEnv.getElementsAnnotatedWith(JsonDeserialize.class).stream())
                    .forEach( annotatedElement -> {
                        if (annotatedElement.getKind() == ElementKind.CLASS) {
                            TypeElement typeElement = (TypeElement) annotatedElement;
                            new Inspector().addTypeElement(typeElement);
                            sourceTypes.add(typeElement.asType().toString());
                        }
                    });


            SerializerGenerator generator = new SerializerGenerator(processedSerializers);
            SchemaGenerator schemaGenerator = new SchemaGenerator(schema_dir);
            DeserializerGenerator deserializerGenerator = new DeserializerGenerator(processedDeserializers);
            for (String typeName : sourceTypes) {
                BeanDescription bean = Inspector.beansCache.get(typeName);
                try {
                    if (!processedSerializers.containsKey(typeName)) {
                        SerializationInfo serializationInfo = generator.generateSerializer(bean);
                        if (autoRegistration) {
                            attachSerializer(bean, serializationInfo);
                        }
                    }
                    if (!processedDeserializers.containsKey(typeName)) {
                        DeserializationInfo deserializationInfo = deserializerGenerator.generateDeserializer(bean);
                        if (autoRegistration) {
                            attachDeserializer(bean, deserializationInfo);
                        }
                    }
                    schemaGenerator.generateSchema(bean);
                } catch (Exception e1) {
                    messager.printMessage(Diagnostic.Kind.WARNING,
                            "Error during generation for " + bean.getSimpleName());
                    if (DEBUG) {
                        e1.printStackTrace();
                    }
                    Utils.warning(messager, null, e1.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Utils.warning(messager, null, e.getMessage());
        }
        return true;
    }


    private void attachClass(BeanDescription beanDescription, JavaFile javaFile, Class<?> annotationName) {
        JCTree.JCExpression selector = treeMaker.Ident(names.fromString(javaFile.packageName));
        JCTree.JCExpression serializer = treeMaker.Select(selector, names.fromString(javaFile.typeSpec.name));
        TypeElement element = beanDescription.getElement();

        Optional<? extends AnnotationMirror> annotation = Utils.getAnnotation(element, annotationName);
        annotation.ifPresent((ann) -> {
            JCTree.JCAnnotation annotationTree = ((JCTree.JCAnnotation)trees.getTree(element, ann));
            JCTree.JCAssign usingAssign = null;
            for (int i = 0; i < annotationTree.args.size(); i++) {
                JCTree.JCExpression annValue = annotationTree.args.get(i);
                if (!"ASSIGN".equals(annValue.getTag().name()))
                    continue;
                if ("using".equals(((JCTree.JCIdent)((JCTree.JCAssign) annValue).getVariable()).name.toString())) {
                    usingAssign = (JCTree.JCAssign) annValue;
                }
            }
            JCTree.JCFieldAccess newValue = treeMaker.Select(serializer, names._class);
            JCTree.JCAssign attribute = treeMaker.Assign(ident("using"), newValue);
            if (usingAssign == null) {
                annotationTree.args = annotationTree.args.prepend(attribute);
            } else { // don't override existing binding
                //usingAssign.rhs = newValue;
            }
        });
    }

    private void attachSerializer(BeanDescription beanDescription, SerializationInfo serializationInfo) {
       attachClass(beanDescription, serializationInfo.getSerializerFile(), JsonSerialize.class);
    }


    private void attachDeserializer(BeanDescription beanDescription, DeserializationInfo serializationInfo) {
        attachClass(beanDescription, serializationInfo.getJavaFile(), JsonDeserialize.class);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


}
