package com.vi34.generation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.squareup.javapoet.*;
import com.vi34.GenerationException;
import com.vi34.beans.BeanDescription;
import com.vi34.beans.ContainerProp;
import com.vi34.beans.MapProp;
import com.vi34.beans.Property;
import com.vi34.utils.Utils;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Map;

/**
 * Created by vi34 on 10/02/2017.
 */
public class SerializerGenerator {

    public static final String SUFFIX = "Serializer";
    private Messager messager;

    private Filer filer;
    private Map<String, SerializationInfo> processed;
    private Map<String, BeanDescription> beansInfo;

    public SerializerGenerator(Filer filer, Messager messager, Map<String, SerializationInfo> processed, Map<String, BeanDescription> beansInfo) {
        this.filer = filer;
        this.processed = processed;
        this.beansInfo = beansInfo;
        this.messager = messager;

    }

    public SerializationInfo generateSerializer(BeanDescription unit) throws IOException, GenerationException {
        SerializationInfo currentSerializationInfo = new SerializationInfo(unit.getTypeName());
        ClassName stdSerializer = ClassName.get(StdSerializer.class);
        ClassName beanClass = ClassName.get(unit.getPackageName(), unit.getSimpleName());

        MethodSpec serialize = MethodSpec.methodBuilder("serialize")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(beanClass, "value")
                .addParameter(JsonGenerator.class, "gen")
                .addParameter(SerializerProvider.class, "provider")
                .returns(void.class)
                .addException(IOException.class)
                .addStatement("$L(value, gen, provider)", writeMethodName(unit.getSimpleName()))
                .build();

        TypeSpec.Builder serializerBuilder = TypeSpec.classBuilder(unit.getSimpleName() + SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(stdSerializer, beanClass))
                .addSuperinterface(ClassName.get(ResolvableSerializer.class))
                .addMethod(serialize);

        addConstructors(serializerBuilder, beanClass);

        MethodSpec serImpl = addSerializeImplementation(unit, currentSerializationInfo, beanClass);
        serializerBuilder.addMethod(serImpl);

        for (SerializationInfo propInfo : currentSerializationInfo.getProps().values()) {
            serializerBuilder.addMethod(propInfo.getSerializeMethod());
        }
        currentSerializationInfo.getStrings().forEach((k, v) -> {
            FieldSpec constDef = FieldSpec.builder(SerializedString.class, k,
                    Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                    .initializer("new SerializedString($S)", v)
                    .build();
            serializerBuilder.addField(constDef);
        });

        addResolve(serializerBuilder, currentSerializationInfo);

        JavaFile javaFile = JavaFile.builder(unit.getPackageName(), serializerBuilder.build())
                .build();

        javaFile.writeTo(filer);
        currentSerializationInfo.setSerializeMethod(serImpl);
        processed.put(unit.getTypeName(), currentSerializationInfo);

        return currentSerializationInfo;
    }

    private MethodSpec addSerializeImplementation(BeanDescription unit, SerializationInfo currentSerializationInfo, ClassName beanClass) throws IOException, GenerationException {
        MethodSpec.Builder serializeImpl = MethodSpec.methodBuilder(writeMethodName(unit.getSimpleName()))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(beanClass, "value")
                .addParameter(JsonGenerator.class, "gen")
                .addParameter(SerializerProvider.class, "provider")
                .returns(void.class)
                .addException(IOException.class)
                .addStatement("gen.writeStartObject()");

        for (Property property : unit.getProps()) {
            addProperty(property, serializeImpl, currentSerializationInfo, "value", true);
        }

        serializeImpl.addStatement("gen.writeEndObject()");
        return serializeImpl.build();
    }

    private void addConstructors(TypeSpec.Builder serializerBuilder, ClassName beanClass) {
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

        serializerBuilder
                .addMethod(constrOneArg)
                .addMethod(constrDef);
    }


    private void addResolve(TypeSpec.Builder serializerBuilder, SerializationInfo currentInfo) {
        MethodSpec.Builder resolve = MethodSpec.methodBuilder("resolve")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(SerializerProvider.class, "provider")
                .returns(void.class)
                .addException(JsonMappingException.class);

        currentInfo.getProvided().forEach((k, v) -> {
            ParameterizedTypeName type = ParameterizedTypeName.get(ClassName.get(JsonSerializer.class), ClassName.get(Object.class));
            String serName = convertToSerializerName(k);
            FieldSpec serDef = FieldSpec.builder(type, serName, Modifier.PRIVATE)
                    .build();
            serializerBuilder.addField(serDef);

            resolve.addStatement("$L = provider.findValueSerializer($T.class)", serName, v);
        });

        serializerBuilder.addMethod(resolve.build());


    }

    private String writeMethodName(String name) {
        return "write_"+name.toLowerCase();
    }

    //TODO handle AtomicLong, BigDecimal, ...
    // TODO handle null options
    // TODO byte arrays - writeBinary
    void addProperty(Property property, MethodSpec.Builder serialize, SerializationInfo current, String varName, boolean named) throws IOException, GenerationException {
        String name = property.getName();
        if (named) {
            String constName = convertToConstName(name);
            serialize.addStatement("gen.writeFieldName($L)", constName);
            current.getStrings().put(constName, name);
        }
        if (property instanceof ContainerProp) {
            Property elem = ((ContainerProp) property).getElem();
            String var = ""+elem.getName().toLowerCase().charAt(0);
            serialize
                    .beginControlFlow("if ($L != null)", property.getAccessor(varName))
                    .addStatement("gen.writeStartArray()")
                    .beginControlFlow("for ($T $L : $L) ", elem.getTName()
                                        , var, property.getAccessor(varName));
            addProperty(elem, serialize, current, var, false);
            serialize
                    .endControlFlow()
                    .addStatement("gen.writeEndArray()")
                    .nextControlFlow("else")
                    .addStatement("gen.writeNull()")
                    .endControlFlow();
        } else if (property instanceof MapProp) {
            MapProp.KeyProp key = ((MapProp) property).getKey();
            Property value = ((MapProp) property).getValue();
            String var = property.getName() + "_entry";
            serialize
                    .beginControlFlow("if ($L != null)", property.getAccessor(varName))
                    .addStatement("gen.writeStartObject()")
                    .beginControlFlow("for ($T<$T,$T> $L : $L.entrySet())", ClassName.get(Map.Entry.class),
                            key.getTName(), value.getTName(), var, property.getAccessor(varName))
                    .addStatement("gen.writeFieldName($L)", key.getAccessor(var));
            addProperty(value, serialize, current, var, false);

            serialize
                    .endControlFlow()
                    .addStatement("gen.writeEndObject()")
                    .nextControlFlow("else")
                    .addStatement("gen.writeNull()")
                    .endControlFlow();
        } else if (property.isSimple()) {
            serialize.addStatement("gen.$L($L)", property.genMethod(), property.getAccessor(varName));
        } else {
            SerializationInfo serInfo = processed.get(property.getTypeName());
            if (serInfo == null) {
                BeanDescription beanDef = beansInfo.get(property.getTypeName());
                if (beanDef == null) {
                    serInfo = new SerializationInfo(property.getTypeName());
                    serInfo.serializeMethod = MethodSpec.methodBuilder(writeMethodName(property.getName()))
                            .addParameter(property.getTName(), "value")
                            .addParameter(JsonGenerator.class, "gen")
                            .addParameter(SerializerProvider.class, "provider")
                            .addStatement("$L.serialize(value, gen, provider)", convertToSerializerName(property.getName()))
                            .addException(IOException.class)
                            .build();

                    current.getProvided().putIfAbsent(property.getTypeName(), property.getTName());
                    Utils.warning(messager, null, "couldn't statically resolve serializer for %s", property.getTypeName());
                } else {
                    serInfo = generateSerializer(beanDef);
                }
            }
            serialize.beginControlFlow("if ($L != null)", property.getAccessor(varName))
                    .addStatement("$L($L, gen, provider)", serInfo.getSerializeMethod().name, property.getAccessor(varName))
                    .nextControlFlow("else")
                    .addStatement("gen.writeNull()")
                    .endControlFlow();
            processed.putIfAbsent(serInfo.getTypeName(), serInfo);
            current.getProps().putIfAbsent(serInfo.getTypeName(), serInfo);
            current.getStrings().putAll(serInfo.getStrings());
            current.getProvided().putAll(serInfo.getProvided());
        }
    }

    private String convertToConstName(String name) {
        return "FIELD_" + name.toUpperCase();
    }

    private String convertToSerializerName(String typeName) {
        String simple = Utils.qualifiedToSimple(typeName);
        return Character.toLowerCase(simple.charAt(0)) + simple.substring(1) + "Serializer";
    }

}
