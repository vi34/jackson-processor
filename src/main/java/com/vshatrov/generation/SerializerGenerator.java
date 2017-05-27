package com.vshatrov.generation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.squareup.javapoet.*;
import com.vshatrov.GenerationException;
import com.vshatrov.beans.BeanDescription;
import com.vshatrov.beans.properties.ContainerProp;
import com.vshatrov.beans.properties.MapProp;
import com.vshatrov.beans.properties.Property;
import com.vshatrov.utils.Utils;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Map;

import static com.vshatrov.utils.Utils.*;

/**
 * Generates JsonSerializer implementation source code, based on given {@link BeanDescription}
 * @author Viktor Shatrov.
 */
public class SerializerGenerator {

    public static final String SUFFIX = "Serializer";
    public static final String PACKAGE_MODIFIER = ".generated.serializers"; // in case of different package, miss package-access values
    private Map<String, SerializationInfo> processed;
    private SerializationInfo currentSerializationInfo;

    public SerializerGenerator(Map<String, SerializationInfo> processed) {
        this.processed = processed;
    }

    public SerializationInfo generateSerializer(BeanDescription unit) throws IOException, GenerationException {
        currentSerializationInfo = new SerializationInfo(unit);
        ClassName stdSerializer = ClassName.get(StdSerializer.class);
        ClassName beanClass = ClassName.get(unit.getPackageName(), unit.getSimpleName());

        TypeSpec.Builder serializerClassBuilder = defineClass(unit, stdSerializer, beanClass);

        addConstructors(serializerClassBuilder, beanClass);
        addSerializeImplementation(serializerClassBuilder, beanClass);
        addConstants(serializerClassBuilder);
        addResolve(serializerClassBuilder);
        addTyping(serializerClassBuilder, beanClass);

        JavaFile javaFile = JavaFile.builder(getPackageName(unit), serializerClassBuilder.build())
                .indent("    ")
                .build();

        javaFile.writeTo(filer);
        currentSerializationInfo.setJavaFile(javaFile);
        processed.put(unit.getTypeName(), currentSerializationInfo);

        return currentSerializationInfo;
    }

    private void addTyping(TypeSpec.Builder serializerClassBuilder, ClassName beanClass) {
        MethodSpec serializeWithType = MethodSpec.methodBuilder("serializeWithType")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(beanClass, "value")
                .addParameter(JsonGenerator.class, "gen")
                .addParameter(SerializerProvider.class, "provider")
                .addParameter(TypeSerializer.class, "typeSer")
                .addException(IOException.class)
                .addStatement("typeSer.writeTypePrefixForObject(value, gen)")
                .addStatement("$L(value, gen, provider)", writeMethodName(currentSerializationInfo.getUnit().getSimpleName()))
                .addStatement("typeSer.writeTypeSuffixForObject(value, gen)")
                .build();
        serializerClassBuilder.addMethod(serializeWithType);
    }

    public void addConstants(TypeSpec.Builder serializerClassBuilder) {
        currentSerializationInfo.getStrings().forEach((k, v) -> {
            FieldSpec constDef = FieldSpec.builder(SerializedString.class, k,
                    Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                    .initializer("new SerializedString($S)", v)
                    .build();
            serializerClassBuilder.addField(constDef);
        });
    }

    public TypeSpec.Builder defineClass(BeanDescription unit, ClassName stdSerializer, ClassName beanClass) {
        MethodSpec serialize = MethodSpec.methodBuilder("serialize")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(beanClass, "value")
                .addParameter(JsonGenerator.class, "gen")
                .addParameter(SerializerProvider.class, "provider")
                .returns(void.class)
                .addException(IOException.class)
                .addStatement("gen.writeStartObject()")
                .addStatement("$L(value, gen, provider)", writeMethodName(unit.getSimpleName()))
                .addStatement("gen.writeEndObject()")
                .build();

        return TypeSpec.classBuilder(unit.getSimpleName() + SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(stdSerializer, beanClass))
                .addSuperinterface(ClassName.get(ResolvableSerializer.class))
                .addMethod(serialize);
    }

    private void addSerializeImplementation(TypeSpec.Builder serializerBuilder,  ClassName beanClass) throws IOException, GenerationException {
        MethodSpec.Builder serializeImpl = MethodSpec.methodBuilder(writeMethodName(currentSerializationInfo.getUnit().getSimpleName()))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(beanClass, "value")
                .addParameter(JsonGenerator.class, "gen")
                .addParameter(SerializerProvider.class, "provider")
                .returns(void.class)
                .addException(IOException.class);

        for (Property property : currentSerializationInfo.getUnit().getProps()) {

            addPropertyName(serializeImpl, property.getName());
            addProperty(property, serializeImpl, "value");
        }

        serializerBuilder.addMethod(serializeImpl.build());
    }

    private void addConstructors(TypeSpec.Builder serializerBuilder, ClassName beanClass) {
        ParameterizedTypeName classBean = ParameterizedTypeName.get(ClassName.get(Class.class), beanClass);
        MethodSpec constrOneArg = MethodSpec.constructorBuilder()
                .addParameter(ParameterSpec.builder(classBean, "t").build())
                .addStatement("super(t)")
                .addModifiers(Modifier.PROTECTED)
                .build();

        MethodSpec constrDef = MethodSpec.constructorBuilder()
                .addStatement("this($T.class)", currentSerializationInfo.getUnit().getClassName())
                .addModifiers(Modifier.PUBLIC)
                .build();

        serializerBuilder
                .addMethod(constrOneArg)
                .addMethod(constrDef);
    }


    private void addResolve(TypeSpec.Builder serializerBuilder) {
        MethodSpec.Builder resolve = MethodSpec.methodBuilder("resolve")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(SerializerProvider.class, "provider")
                .returns(void.class)
                .addException(JsonMappingException.class)
                .addStatement("$T javaType", ClassName.get(JavaType.class))
                .addStatement("$1T typeFactory = $1T.defaultInstance()", ClassName.get(TypeFactory.class));

        currentSerializationInfo.getProvided().forEach((typeName) -> {
            ParameterizedTypeName type = ParameterizedTypeName.get(ClassName.get(JsonSerializer.class), ClassName.get(Object.class));
            String serName = convertToSerializerName(typeName);
            FieldSpec serDef = FieldSpec.builder(type, serName, Modifier.PRIVATE)
                    .build();
            serializerBuilder.addField(serDef);

            resolve.addStatement("javaType = typeFactory.constructType(new $T<$T>(){})",
                    ClassName.get(TypeReference.class), typeName);
            resolve.addStatement("$L = provider.findValueSerializer(javaType)", serName);
        });

        serializerBuilder.addMethod(resolve.build());

    }

    // TODO handle null options
    // TODO byte arrays - writeBinary
    /**
     * adds serializing code to {@param method}.
     * @param property - property which belongs to object being serialized
     * @param objectVarName Name of variable of object which being serialized
     */
    void addProperty(Property property, MethodSpec.Builder method, String objectVarName) throws IOException, GenerationException {
        String propVar = property.getName() + "_var";
        method.addStatement("$T $L = $L", property.getTName(), propVar, property.getAccessor(objectVarName));

        if (property instanceof ContainerProp) {
            writeContainer((ContainerProp)property, method, propVar);
        } else if (property instanceof MapProp) {
            writeMap((MapProp) property, method, propVar);
        } else if (property.isSimple()) {
            method.addStatement("gen.$L", property.writeMethod(objectVarName));
        } else {
            // use runtime resolved serializer
            method.beginControlFlow("if ($L != null)", propVar)
                    .addStatement("$L.serialize($L, gen, provider)", convertToSerializerName(property.getTName()), propVar)
                    .nextControlFlow("else")
                    .addStatement("gen.writeNull()")
                    .endControlFlow();
            currentSerializationInfo.getProvided().add(property.getTName());
        }
    }

    private void addPropertyName(MethodSpec.Builder method, String name) {
        String constName = convertToConstName(name);
        method.addStatement("gen.writeFieldName($L)", constName);
        currentSerializationInfo.getStrings().put(constName, name);
    }

    private void writeMap(MapProp property, MethodSpec.Builder method, String propVarName) throws GenerationException, IOException {
        MapProp.KeyProp key = property.getKey();
        Property value = property.getValue();
        String var = property.getName() + "_entry";
        method
                .beginControlFlow("if ($L != null)", propVarName)
                .addStatement("gen.writeStartObject()")
                .beginControlFlow("for ($T<$T,$T> $L : $L.entrySet())", ClassName.get(Map.Entry.class),
                        key.getTName(), value.getTName(), var, propVarName)
                .addStatement("gen.writeFieldName($L)", key.writeMethod(var));

        addProperty(value, method, var);

        method
                .endControlFlow()
                .addStatement("gen.writeEndObject()")
                .nextControlFlow("else")
                .addStatement("gen.writeNull()")
                .endControlFlow();
    }

    private void writeContainer(ContainerProp property, MethodSpec.Builder method, String propVarName) throws IOException, GenerationException {
        Property element = property.getElement();
        String elemVar = ""+element.getName().toLowerCase().charAt(0);
        method
                .beginControlFlow("if ($L != null)", propVarName)
                .addStatement("gen.writeStartArray()")
                .beginControlFlow("for ($T $L : $L) ", element.getTName(), elemVar, propVarName);

        addProperty(element, method, elemVar);

        method
                .endControlFlow()
                .addStatement("gen.writeEndArray()")
                .nextControlFlow("else")
                .addStatement("gen.writeNull()")
                .endControlFlow();
    }


    public static String getPackageName(BeanDescription unit) {
        return "com.vshatrov" + PACKAGE_MODIFIER;
        //return unit.getPackageName() + PACKAGE_MODIFIER;
    }

    private String writeMethodName(String name) {
        return "write_"+name.toLowerCase();
    }

    private String convertToConstName(String name) {
        return "FIELD_" + name.toUpperCase();
    }

    private String convertToSerializerName(TypeName typeName) {
        String simple = Utils.qualifiedToSimple(typeName.toString());
        return Character.toLowerCase(simple.charAt(0)) + simple.substring(1) + "Serializer";
    }

}
