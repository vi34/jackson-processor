package com.vi34;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;

/**
 * Created by vi34 on 22/12/2016.
 */
public class SerializationUnit {

    public static final String SUFFIX = "Serializator";
    private String qualifiedName;

    SerializationUnit(TypeElement element) {
        qualifiedName = element.getQualifiedName().toString();
    }

    void generateSerializator(Elements elementUtils, Filer filer) throws IOException {
        TypeSpec helloWorld = TypeSpec.classBuilder(qualifiedName + SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .build();

        JavaFile javaFile = JavaFile.builder("com.vi34", helloWorld)
                .build();

        javaFile.writeTo(filer);

    }
}
