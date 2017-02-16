package com.vi34;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import java.io.IOException;

/**
 * Created by vi34 on 10/02/2017.
 */
public class SerializerGenerator {

    public static final String SUFFIX = "Serializator";

    Filer filer;
    Elements elementUtils;


    public SerializerGenerator(Elements elementUtils, Filer filer) {
        this.filer = filer;
        this.elementUtils = elementUtils;
    }

    void generateSerializer(SerializationUnit unit) throws IOException {
        TypeSpec helloWorld = TypeSpec.classBuilder(unit.getSimpleName() + SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .build();

        JavaFile javaFile = JavaFile.builder("com.vi34", helloWorld)
                .build();

        javaFile.writeTo(filer);

    }
}
