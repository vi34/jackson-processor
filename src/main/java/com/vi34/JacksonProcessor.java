package com.vi34;

import com.google.auto.service.AutoService;
import com.vi34.annotations.GenerateClasses;
import com.vi34.beans.BeanDescription;
import com.vi34.beans.Inspector;
import com.vi34.generation.SchemaGenerator;
import com.vi34.generation.SerializationInfo;
import com.vi34.generation.SerializerGenerator;
import com.vi34.utils.Utils;

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
 * Created by vi34 on 22/12/2016.
 */
@AutoService(javax.annotation.processing.Processor.class)
@SupportedAnnotationTypes({
        "com.vi34.annotations.GenerateClasses"
})
public class JacksonProcessor extends AbstractProcessor {

    private static final boolean DEBUG = true;
    public static final String DEFAULT_SCHEMA_DIR = "./json-schema";
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Map<String, SerializationInfo> processed;
    private Map<String, BeanDescription> beansInfo;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        processed = new HashMap<>();
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
            SerializerGenerator generator = new SerializerGenerator(filer, messager, processed, beansInfo);
            String schema_dir = processingEnv.getOptions().getOrDefault("SCHEMA_DIR", DEFAULT_SCHEMA_DIR);
            SchemaGenerator schemaGenerator = new SchemaGenerator(schema_dir);
            for (Map.Entry<String, BeanDescription> e : beansInfo.entrySet()) {
                try {
                    if (!processed.containsKey(e.getKey())) {
                        generator.generateSerializer(e.getValue());
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
