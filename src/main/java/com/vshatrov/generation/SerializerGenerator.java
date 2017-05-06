package com.vshatrov.generation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
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
import java.util.Map;

import static com.vshatrov.utils.Utils.*;

/**
 * @author Viktor Shatrov.
 */
public class SerializerGenerator {

    //TODO: correct Character serialization/deserialization
    //TODO: java.util.Date

    public static final String SUFFIX = "Serializer";
    //TODO: investigate package fields access
    public static final String PACKAGE_MODIFIER = ""; // in case of different package, miss package-access values
    private Map<String, SerializationInfo> processed;
    private SerializationInfo currentSerializationInfo;

    public SerializerGenerator(Map<String, SerializationInfo> processed) {
        this.processed = processed;
    }

    public SerializationInfo generateSerializer(BeanDescription unit) throws IOException, GenerationException {
        currentSerializationInfo = new SerializationInfo(unit.getTypeName());
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

        TypeSpec.Builder serializerClassBuilder = TypeSpec.classBuilder(unit.getSimpleName() + SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(stdSerializer, beanClass))
                .addSuperinterface(ClassName.get(ResolvableSerializer.class))
                .addMethod(serialize);

        addConstructors(serializerClassBuilder, beanClass);

        MethodSpec serImpl = addSerializeImplementation(unit, beanClass);
        serializerClassBuilder.addMethod(serImpl);

        currentSerializationInfo.getStrings().forEach((k, v) -> {
            FieldSpec constDef = FieldSpec.builder(SerializedString.class, k,
                    Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                    .initializer("new SerializedString($S)", v)
                    .build();
            serializerClassBuilder.addField(constDef);
        });

        addResolve(serializerClassBuilder);

        JavaFile javaFile = JavaFile.builder(unit.getPackageName() + PACKAGE_MODIFIER, serializerClassBuilder.build())
                .indent("    ")
                .build();

        javaFile.writeTo(filer);
        currentSerializationInfo.setSerializeMethod(serImpl);
        currentSerializationInfo.setSerializerFile(javaFile);
        processed.put(unit.getTypeName(), currentSerializationInfo);

        return currentSerializationInfo;
    }

    private MethodSpec addSerializeImplementation(BeanDescription unit, ClassName beanClass) throws IOException, GenerationException {
        MethodSpec.Builder serializeImpl = MethodSpec.methodBuilder(writeMethodName(unit.getSimpleName()))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(beanClass, "value")
                .addParameter(JsonGenerator.class, "gen")
                .addParameter(SerializerProvider.class, "provider")
                .returns(void.class)
                .addException(IOException.class)
                .addStatement("gen.writeStartObject()");

        for (Property property : unit.getProps()) {
            addProperty(property, serializeImpl, "value", true);
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


    private void addResolve(TypeSpec.Builder serializerBuilder) {
        MethodSpec.Builder resolve = MethodSpec.methodBuilder("resolve")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(SerializerProvider.class, "provider")
                .returns(void.class)
                .addException(JsonMappingException.class)
                .addStatement("$T javaType", ClassName.get(JavaType.class))
                .addStatement("$1T typeFactory = $1T.defaultInstance()", ClassName.get(TypeFactory.class));

        currentSerializationInfo.getProvided().forEach((prop) -> {
            ParameterizedTypeName type = ParameterizedTypeName.get(ClassName.get(JsonSerializer.class), ClassName.get(Object.class));
            String serName = convertToSerializerName(prop);
            FieldSpec serDef = FieldSpec.builder(type, serName, Modifier.PRIVATE)
                    .build();
            serializerBuilder.addField(serDef);

            resolve.addStatement("javaType = typeFactory.constructType(new $T<$T>(){})",
                    ClassName.get(TypeReference.class), prop.getTName());
            resolve.addStatement("$L = provider.findValueSerializer(javaType)", serName);
        });

        serializerBuilder.addMethod(resolve.build());


    }

    private String writeMethodName(String name) {
        return "write_"+name.toLowerCase();
    }

    //TODO handle AtomicLong, BigDecimal, ...
    // TODO handle null options
    // TODO byte arrays - writeBinary
    /**
     * adds serializing code to {@param method}.
     * @param property - property which belongs to object being serialized
     * @param objectVarName Name of variable of object which being serialized
     * @param named - specifies if property name writing must be added
     */
    void addProperty(Property property, MethodSpec.Builder method, String objectVarName, boolean named) throws IOException, GenerationException {
        String name = property.getName();
        if (named) {
            String constName = convertToConstName(name);
            method.addStatement("gen.writeFieldName($L)", constName);
            currentSerializationInfo.getStrings().put(constName, name);
        }
        String propVar = property.getName() + "_var";
        method.addStatement("$T $L = $L", property.getTName(), propVar, property.getAccessor(objectVarName));
        if (property instanceof ContainerProp) {
            writeContainer((ContainerProp)property, method, propVar);
        } else if (property instanceof MapProp) {
            writeMap((MapProp) property, method, propVar);
        } else if (property.isSimple()) {
            method.addStatement("gen.$L", property.genMethod(objectVarName));
        } else {
            method.beginControlFlow("if ($L != null)", propVar)
                    .addStatement("$L.serialize($L, gen, provider)", convertToSerializerName(property), propVar)
                    .nextControlFlow("else")
                    .addStatement("gen.writeNull()")
                    .endControlFlow();
            currentSerializationInfo.getProvided().add(property);
        }
    }

    private void writeMap(MapProp property, MethodSpec.Builder method, String propVarName) throws GenerationException, IOException {
        MapProp.KeyProp key =  property.getKey();
        Property value = property.getValue();
        String var = property.getName() + "_entry";
        method
                .beginControlFlow("if ($L != null)", propVarName)
                .addStatement("gen.writeStartObject()")
                .beginControlFlow("for ($T<$T,$T> $L : $L.entrySet())", ClassName.get(Map.Entry.class),
                        key.getTName(), value.getTName(), var, propVarName)
                .addStatement("gen.writeFieldName($L)", key.genMethod(var));

        addProperty(value, method, var, false);

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

        addProperty(element, method, elemVar, false);

        method
                .endControlFlow()
                .addStatement("gen.writeEndArray()")
                .nextControlFlow("else")
                .addStatement("gen.writeNull()")
                .endControlFlow();
    }

    private String convertToConstName(String name) {
        return "FIELD_" + name.toUpperCase();
    }

    private String convertToSerializerName(Property property) {
        String simple = Utils.qualifiedToSimple(property.getTypeName());
        return Character.toLowerCase(simple.charAt(0)) + simple.substring(1) + "Serializer";
    }

}
