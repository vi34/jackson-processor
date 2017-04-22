package com.vshatrov;

import com.google.auto.service.AutoService;
import com.vshatrov.annotations.GenerateClasses;
import com.vshatrov.beans.BeanDescription;
import com.vshatrov.beans.Inspector;
import com.vshatrov.generation.*;
import com.vshatrov.utils.Utils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Viktor Shatrov.
 */
@AutoService(javax.annotation.processing.Processor.class)
@SupportedAnnotationTypes({
        "com.vshatrov.annotations.*"
})
public class JacksonProcessor extends AbstractProcessor {

    private static final boolean DEBUG = true;
    public static final String DEFAULT_SCHEMA_DIR = "./json-schema";
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Map<String, SerializationInfo> processedSerializers;
    private Map<String, DeserializationInfo> processedDeserializers;
    private Map<String, BeanDescription> beansInfo;

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
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            for (Element annotatedElement: roundEnv.getElementsAnnotatedWith(GenerateClasses.class)) {
                if (annotatedElement.getKind() != ElementKind.CLASS) {
                    Utils.warning(messager, annotatedElement, "Only classes can be annotated with @%s", GenerateClasses.class.getSimpleName());
                    continue;
                }
                TypeElement typeElement = (TypeElement) annotatedElement;
                Inspector inspector = new Inspector(elementUtils);
                BeanDescription beanDescription = inspector.inspect(typeElement);
                beansInfo.put(beanDescription.getTypeName(), beanDescription);
            }
            SerializerGenerator generator = new SerializerGenerator(filer, messager, processedSerializers, beansInfo);
            String schema_dir = processingEnv.getOptions().getOrDefault("SCHEMA_DIR", DEFAULT_SCHEMA_DIR);
            SchemaGenerator schemaGenerator = new SchemaGenerator(schema_dir);
            DeserializerGenerator deserializerGenerator = new DeserializerGenerator(filer, messager, processedDeserializers, beansInfo);
            for (Map.Entry<String, BeanDescription> e : beansInfo.entrySet()) {
                try {
                    if (!processedSerializers.containsKey(e.getKey())) {
                        generator.generateSerializer(e.getValue());
                    }
                    if (!processedDeserializers.containsKey(e.getKey())) {
                        deserializerGenerator.generateDeserializer(e.getValue());
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

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
