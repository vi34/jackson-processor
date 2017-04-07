package com.vi34;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.squareup.javapoet.*;
import com.vi34.beans.BeanDefinition;
import com.vi34.beans.ContainerProp;
import com.vi34.beans.Property;
import com.vi34.beans.SerializeInfo;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.Map;

/**
 * Created by vi34 on 10/02/2017.
 */
public class SerializerGenerator {

    public static final String SUFFIX = "Serializer";

    Filer filer;
    Elements elementUtils;
    Map<String, SerializeInfo> processed;
    Map<String, BeanDefinition> beansInfo;

    public SerializerGenerator(Elements elementUtils, Filer filer, Map<String, SerializeInfo> processed, Map<String, BeanDefinition> beansInfo) {
        this.filer = filer;
        this.elementUtils = elementUtils;
        this.processed = processed;
        this.beansInfo = beansInfo;
    }

    SerializeInfo generateSerializer(BeanDefinition unit) throws IOException, GenerationException {
        SerializeInfo currentSerializeInfo = new SerializeInfo(unit.getTypeName());
        ClassName stdSerializer = ClassName.get("com.fasterxml.jackson.databind.ser.std","StdSerializer");
        ClassName beanClass = ClassName.get(unit.getPackageName(), unit.getSimpleName());

        MethodSpec serialize = MethodSpec.methodBuilder("serialize")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(beanClass, "value")
                .addParameter(JsonGenerator.class, "gen")
                .addParameter(SerializerProvider.class, "provider")
                .returns(void.class)
                .addException(IOException.class)
                .addStatement("$L(value, gen, provider)", unit.getSimpleName().toLowerCase())
                .build();

        MethodSpec.Builder serializeImpl = MethodSpec.methodBuilder(unit.getSimpleName().toLowerCase())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(beanClass, "value")
                .addParameter(JsonGenerator.class, "gen")
                .addParameter(SerializerProvider.class, "provider")
                .returns(void.class)
                .addException(IOException.class)
                .addStatement("gen.writeStartObject()");


        ParameterizedTypeName classBean = ParameterizedTypeName.get(ClassName.get(Class.class), beanClass);
        MethodSpec constrOneArg = MethodSpec.constructorBuilder()
                .addParameter(ParameterSpec.builder(classBean, "t").build())
                .addStatement("super(t)")
                .addModifiers(Modifier.PROTECTED)
                .build();

        MethodSpec constrDef = MethodSpec.constructorBuilder()
                .addStatement("this(null)")
                .addModifiers(Modifier.PUBLIC)
                .build();

        TypeSpec.Builder serializerBuilder = TypeSpec.classBuilder(unit.getSimpleName() + SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(stdSerializer, beanClass))
                .addMethod(constrOneArg)
                .addMethod(constrDef)
                .addMethod(serialize);

        for (Property property : unit.getProps()) {
            addProp(property, serializeImpl, serializerBuilder, currentSerializeInfo, true);
        }

        serializeImpl.addStatement("gen.writeEndObject()");
        MethodSpec serImpl = serializeImpl.build();

        for (SerializeInfo propInfo : currentSerializeInfo.getProps().values()) {
            serializerBuilder.addMethod(propInfo.getSerializeMethod());
        }
        TypeSpec serializer = serializerBuilder.addMethod(serImpl).build();


        JavaFile javaFile = JavaFile.builder("com.vi34", serializer)
                .build();

        javaFile.writeTo(filer);
        currentSerializeInfo.setSerializeMethod(serImpl);
        processed.put(unit.getTypeName(), currentSerializeInfo);
        return currentSerializeInfo;
    }

    //TODO handle more types. Watch JsonGenerator._writeSimpleObject
    private String genMethod(Property prop) throws GenerationException {
        if (prop.isNumber()) {
            return "writeNumber";
        }

        switch (prop.getTypeName()) {
            case "boolean":case "java.lang.Boolean": return "writeBoolean";
            case "char":case "java.lang.Character":case "java.lang.String": return "writeString";
        }

        throw new GenerationException("Couldn't find generator method for " + prop);
    }

    //TODO handle AtomicLong, BigDecimal, ...
    private String accessorString(Property prop) {
        if (prop.getAccessor() != null)
            return prop.getAccessor();
        String res = "value." + (prop.isField() ? prop.getName() : prop.getter());

        if (prop.getTypeName().equals("char") || prop.getTypeName().equals("java.lang.Character"))
            res += " + \"\"";
        return res;
    }

    // TODO handle nulls
    void addProp(Property property, MethodSpec.Builder serialize, TypeSpec.Builder serializerBuilder, SerializeInfo current, boolean named) throws IOException, GenerationException {
        String name = property.getName();
        if (named)
            serialize.addStatement("gen.writeFieldName($S)", name);  // TODO SerializedString
        if (property instanceof ContainerProp) {
            serialize.addStatement("gen.writeStartArray()");
            Property elem = ((ContainerProp) property).getElem();
            serialize.beginControlFlow("for ($T $L : $L) ", elem.getTName()
                    , elem.getName().toLowerCase().charAt(0), accessorString(property));
            addProp(elem, serialize, serializerBuilder, current, false);
            serialize.endControlFlow();
            serialize.addStatement("gen.writeEndArray()");
        } else if (property.isSimple()) {
            serialize.addStatement("gen.$L($L)", genMethod(property), accessorString(property));
        } else {
            SerializeInfo serInfo = processed.get(property.getTypeName());
            if (serInfo == null) {
                BeanDefinition beanDef = beansInfo.get(property.getTypeName());
                if (beanDef == null) {
                    // todo unknown class.
                } else {
                    serInfo = generateSerializer(beanDef);
                }
            }
            serialize.addStatement("$L($L, gen, provider)", serInfo.getSerializeMethod().name, accessorString(property));
            current.getProps().putIfAbsent(serInfo.getTypeName(), serInfo);
        }
    }

}
