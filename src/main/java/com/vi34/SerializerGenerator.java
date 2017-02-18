package com.vi34;

import com.squareup.javapoet.*;

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

    void generateSerializer(ClassDefinition unit) throws IOException {
        MethodSpec serialize = MethodSpec.methodBuilder("serialize")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                //.addParameter()
                .returns(void.class)
                .addException(IOException.class)
                .build();


        ClassName stdSerializer = ClassName.get("com.fasterxml.jackson.databind.ser.std","StdSerializer");
        ClassName unitClass = ClassName.get(unit.packageName, unit.simpleName);

        TypeSpec serializer = TypeSpec.classBuilder(unit.getSimpleName() + SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(stdSerializer, unitClass))
                .addMethod(serialize)
                .build();

        JavaFile javaFile = JavaFile.builder("com.vi34", serializer)
                .build();

        javaFile.writeTo(filer);

    }
}
