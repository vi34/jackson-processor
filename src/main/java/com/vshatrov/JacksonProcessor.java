package com.vshatrov;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.sun.source.util.Trees;
import com.sun.tools.javac.model.JavacElements;
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
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.*;

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
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Map<String, SerializationInfo> processedSerializers;
    private Map<String, DeserializationInfo> processedDeserializers;
    private Map<String, BeanDescription> beansInfo;
    private TreeMaker treeMaker;
    private Trees trees;
    private Names names;
    private JavacElements javacElements;
    private boolean autoRegistration;
    private String schema_dir;


    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        processedSerializers = new HashMap<>();
        processedDeserializers = new HashMap<>();
        beansInfo = new HashMap<>();
        Utils.typeUtils = typeUtils;
        trees = Trees.instance(env);
        JavacProcessingEnvironment javacEnv = (JavacProcessingEnvironment) env;
        treeMaker = TreeMaker.instance(javacEnv.getContext());
        names = Names.instance(javacEnv.getContext());
        javacElements = javacEnv.getElementUtils();
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

            for (Element annotatedElement: roundEnv.getElementsAnnotatedWith(JsonSerialize.class)) {
                if (annotatedElement.getKind() != ElementKind.CLASS) {
                    continue;
                }
                TypeElement typeElement = (TypeElement) annotatedElement;
                Inspector inspector = new Inspector(elementUtils);
                BeanDescription beanDescription = inspector.inspect(typeElement);
                beansInfo.put(beanDescription.getTypeName(), beanDescription);
            }

            for (Element annotatedElement: roundEnv.getElementsAnnotatedWith(JsonDeserialize.class)) {
                if (annotatedElement.getKind() != ElementKind.CLASS) {
                    continue;
                }
                TypeElement typeElement = (TypeElement) annotatedElement;
                String typeName = typeElement.asType().toString();
                beansInfo.computeIfAbsent(typeName, (k) -> {
                    Inspector inspector = new Inspector(elementUtils);
                    return inspector.inspect(typeElement);
                });
            }


            SerializerGenerator generator = new SerializerGenerator(filer, messager, processedSerializers, beansInfo);
            SchemaGenerator schemaGenerator = new SchemaGenerator(schema_dir);
            DeserializerGenerator deserializerGenerator = new DeserializerGenerator(filer, messager, processedDeserializers);
            for (Map.Entry<String, BeanDescription> e : beansInfo.entrySet()) {
                try {
                    if (!processedSerializers.containsKey(e.getKey())) {
                        SerializationInfo serializationInfo = generator.generateSerializer(e.getValue());
                        if (autoRegistration) {
                            attachSerializer(e.getValue(), serializationInfo);
                        }
                    }
                    if (!processedDeserializers.containsKey(e.getKey())) {
                        DeserializationInfo deserializationInfo = deserializerGenerator.generateDeserializer(e.getValue());
                        if (autoRegistration) {
                            attachDeserializer(e.getValue(), deserializationInfo);
                        }
                    }
                    schemaGenerator.generateSchema(e.getValue());
                } catch (Exception e1) {
                    messager.printMessage(Diagnostic.Kind.WARNING,
                            "Error during generation for " + e.getValue().getSimpleName());
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


    private void attachClass(BeanDescription beanDescription, JavaFile javaFile, String annotationName) {
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
       attachClass(beanDescription, serializationInfo.getSerializerFile(), JSON_SERIALIZE);
    }


    private void attachDeserializer(BeanDescription beanDescription, DeserializationInfo serializationInfo) {
        attachClass(beanDescription, serializationInfo.getJavaFile(), JSON_DESERIALIZE);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private JCTree.JCIdent ident(String name) {
        return treeMaker.Ident(javacElements.getName(name));
    }

}
