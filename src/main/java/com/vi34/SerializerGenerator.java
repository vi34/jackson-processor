package com.vi34;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import java.io.IOException;

/**
 * Created by vi34 on 10/02/2017.
 */
public class SerializerGenerator {

    public static final String SUFFIX = "Serializer";

    Filer filer;
    Elements elementUtils;


    public SerializerGenerator(Elements elementUtils, Filer filer) {
        this.filer = filer;
        this.elementUtils = elementUtils;
    }

    void generateSerializer(ClassDefinition unit) throws IOException {
        ClassName stdSerializer = ClassName.get("com.fasterxml.jackson.databind.ser.std","StdSerializer");
        ClassName unitClass = ClassName.get(unit.packageName, unit.simpleName);

        MethodSpec.Builder serialize = MethodSpec.methodBuilder("serialize")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(unitClass, "value")
                .addParameter(JsonGenerator.class, "gen")
                .addParameter(SerializerProvider.class, "provider")
                .returns(void.class)
                .addException(IOException.class)
                .addStatement("gen.writeStartObject()");

        for (VariableElement field : unit.fields) {
            addField(field, serialize);
        }

        TypeSpec serializer = TypeSpec.classBuilder(unit.getSimpleName() + SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(stdSerializer, unitClass))
                .addMethod(serialize.build())
                .build();

        JavaFile javaFile = JavaFile.builder("com.vi34", serializer)
                .build();

        javaFile.writeTo(filer);

    }

    void addField(VariableElement field, MethodSpec.Builder serialize) {
        String name = field.getSimpleName().toString();
        TypeKind kind = field.asType().getKind();
        String genMethod;
        switch (kind) {
            case BOOLEAN: genMethod = "writeBooleanField"; break;
            case DOUBLE: case INT: genMethod = "writeNumberField"; break;
            default: //throw exception
                genMethod = null;
        }
        if (genMethod != null) {
            serialize.addStatement("gen.$L($S, value.$L)",genMethod, name, getM(name));
        }
    }


    //temporary method
    private String getM(String name) {
        return "get" + Character.isUpperCase(name.charAt(0)) + name.substring(1) + "()";
    }

}
