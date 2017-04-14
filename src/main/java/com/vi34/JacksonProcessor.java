package com.vi34;

import com.google.auto.service.AutoService;
import com.vi34.annotations.GenerateClasses;
import com.vi34.beans.BeanDescription;
import com.vi34.beans.Inspector;
import com.vi34.generation.SerializationInfo;
import com.vi34.generation.SerializerGenerator;

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
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            for (Element annotatedElement: roundEnv.getElementsAnnotatedWith(GenerateClasses.class)) {
                if (annotatedElement.getKind() != ElementKind.CLASS) {
                    warning(annotatedElement, "Only classes can be annotated with @%s", GenerateClasses.class.getSimpleName());
                    continue;
                }
                TypeElement typeElement = (TypeElement) annotatedElement;
                Inspector inspector = new Inspector(elementUtils, typeUtils);
                BeanDescription beanDescription = inspector.inspect(typeElement);
                beansInfo.put(beanDescription.getTypeName(), beanDescription);
            }
            if (roundEnv.processingOver()) {
                SerializerGenerator generator = new SerializerGenerator(filer, processed, beansInfo);
                for (Map.Entry<String, BeanDescription> e : beansInfo.entrySet()) {
                    try {
                        if (!processed.containsKey(e.getKey())) {
                            generator.generateSerializer(e.getValue());
                        }
                    } catch (Exception e1) {
                        messager.printMessage(Diagnostic.Kind.WARNING,
                                "Error during generation for " + e.getValue().getSimpleName());
                        if (DEBUG) {
                            e1.printStackTrace();
                        }
                        warning(null, e1.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            warning(null, e.getMessage());
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void warning(Element e, String msg, Object... args) {
        if (msg != null) {
            messager.printMessage(
                    Diagnostic.Kind.WARNING,
                    String.format(msg, args),
                    e);
        }
    }

}
