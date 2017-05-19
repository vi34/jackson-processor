package com.vshatrov.generation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import com.fasterxml.jackson.databind.deser.std.PrimitiveArrayDeserializers;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.squareup.javapoet.*;
import com.vshatrov.GenerationException;
import com.vshatrov.beans.BeanDescription;
import com.vshatrov.beans.Inspector;
import com.vshatrov.beans.properties.*;
import com.vshatrov.utils.Utils;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.*;

import static com.vshatrov.utils.Utils.filer;

/**
 * @author Viktor Shatrov.
 */
public class DeserializerGenerator {
    public static final String PACKAGE_MODIFIER = ".generated.deserializers";
    public static final String SUFFIX = "Deserializer";
    public static final String MAPPING_VAR = "fullFieldToIndex";

    private Map<String, DeserializationInfo> processed;

    private DeserializationInfo currentDeserInfo;


    public DeserializerGenerator(Map<String, DeserializationInfo> processed) {
        this.processed = processed;
    }

    public DeserializationInfo generateDeserializer(BeanDescription unit) throws IOException, GenerationException {
        currentDeserInfo = new DeserializationInfo(unit);
        ClassName stdDeserializer = ClassName.get(StdDeserializer.class);

        TypeSpec.Builder deserializerBuilder = defineClass(unit, stdDeserializer);

        addConstructors(deserializerBuilder, unit.getClassName());

        addDeserializeImplementation(deserializerBuilder, unit.getClassName());
        generateConstants(deserializerBuilder);
        addHelperMethods(deserializerBuilder);
        addResolve(deserializerBuilder);

        currentDeserInfo.getReadMethods().values().forEach(deserializerBuilder::addMethod);

        JavaFile javaFile = JavaFile.builder(getPackageName(unit), deserializerBuilder.build())
                .indent("    ")
                .build();

        javaFile.writeTo(filer);
        currentDeserInfo.setJavaFile(javaFile);
        processed.put(unit.getTypeName(), currentDeserInfo);
        return currentDeserInfo;
    }

    public TypeSpec.Builder defineClass(BeanDescription unit, ClassName stdDeserializer) {
        MethodSpec deserialize = MethodSpec.methodBuilder("deserialize")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(JsonParser.class, "parser")
                .addParameter(DeserializationContext.class, "ctxt")
                .returns(unit.getClassName())
                .addException(IOException.class)
                .addStatement("return $L(parser, ctxt)", readMethodName(unit.getSimpleName()))
                .build();

        return TypeSpec.classBuilder(unit.getSimpleName() + SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ParameterizedTypeName.get(stdDeserializer, unit.getClassName()))
                .addSuperinterface(ClassName.get(ResolvableDeserializer.class))
                .addMethod(deserialize);
    }

    private void addHelperMethods(TypeSpec.Builder deserializerBuilder) {
        MethodSpec verify = MethodSpec.methodBuilder("verifyCurrent")
                .addModifiers(Modifier.PRIVATE)
                .returns(void.class)
                .addParameter(JsonParser.class, "parser")
                .addParameter(JsonToken.class, "expToken")
                .addException(IOException.class)
                .addCode("  if (parser.getCurrentToken() != expToken) {\n" +
                        "            reportIllegal(parser, expToken);\n" +
                        "        }\n")
                .build();

        MethodSpec report = MethodSpec.methodBuilder("reportIllegal")
                .addModifiers(Modifier.PRIVATE)
                .returns(void.class)
                .addParameter(JsonParser.class, "parser")
                .addParameter(JsonToken.class, "expToken")
                .addException(IOException.class)
                .addCode(" JsonToken curr = parser.getCurrentToken();\n" +
                        "        String msg = \"Expected token \"+expToken+\"; got \"+curr;\n" +
                        "        if (curr == JsonToken.FIELD_NAME) {\n" +
                        "            msg += \" (current field name '\"+parser.getCurrentName()+\"')\";\n" +
                        "        }\n" +
                        "        msg += \", location: \"+parser.getTokenLocation();\n" +
                        "        throw new IllegalStateException(msg);\n")
                .build();
        deserializerBuilder.addMethod(verify);
        deserializerBuilder.addMethod(report);

    }

    private void generateConstants(TypeSpec.Builder deserializerBuilder) {
        ParameterizedTypeName mapType = ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(String.class), ClassName.get(Integer.class));
        FieldSpec fieldMapping = FieldSpec
                .builder(mapType, MAPPING_VAR)
                .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                .initializer("new $T()", mapType)
                .build();

        deserializerBuilder.addField(fieldMapping);
        CodeBlock.Builder mappingBuilder = CodeBlock.builder();
        for (int i = 0; i < currentDeserInfo.getUnit().getProps().size(); i++) {
            Property prop = currentDeserInfo.getUnit().getProps().get(i);

            FieldSpec fullFieldConst = FieldSpec
                    .builder(String.class, convertToStringConstName(prop.getName()))
                    .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                    .initializer("$S", prop.getName())
                    .build();

            FieldSpec indexFieldConst = FieldSpec
                    .builder(int.class, convertToIndexConstName(prop.getName()))
                    .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                    .initializer("$L", i + 1)
                    .build();

            FieldSpec fieldConst = FieldSpec
                    .builder(SerializedString.class, convertToSerializedConstName(prop.getName()))
                    .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                    .initializer("new SerializedString($L)", convertToStringConstName(prop.getName()))
                    .build();

            mappingBuilder.addStatement("$L.put($L, $L)", MAPPING_VAR, fullFieldConst.name, indexFieldConst.name);

            for (int j = 0; j < prop.getAlternativeNames().size(); ++j) {
                String alterName = prop.getAlternativeNames().get(j);
                FieldSpec altField = FieldSpec
                        .builder(String.class, convertToStringAlternativeName(prop.getName(), alterName, j))
                        .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                        .initializer("$S", alterName)
                        .build();
                deserializerBuilder.addField(altField);
                mappingBuilder.addStatement("$L.put($L, $L)", MAPPING_VAR, altField.name, indexFieldConst.name);
            }

            deserializerBuilder
                    .addField(fullFieldConst)
                    .addField(indexFieldConst)
                    .addField(fieldConst);
        }
        deserializerBuilder.addStaticBlock(mappingBuilder.build());
    }

    private void addDeserializeImplementation(TypeSpec.Builder deserializerBuilder, ClassName beanClass)
            throws IOException, GenerationException {
        MethodSpec.Builder deserializeImpl = MethodSpec.methodBuilder(readMethodName(currentDeserInfo.getUnit().getSimpleName()))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(JsonParser.class, "parser")
                .addParameter(DeserializationContext.class, "ctxt")
                .returns(beanClass)
                .addException(IOException.class);

        String varName = instantiate(beanClass, deserializeImpl);

        currentDeserInfo.getPrimitiveProps().forEach((name, prop) -> {
            deserializeImpl.addStatement("boolean $L = false", presenceFlag(name));
        });

        genFastParsing(deserializeImpl, varName, 0);
        genCommonParsing(deserializeImpl, varName);
        deserializeImpl.addStatement("verifyCurrent(parser, JsonToken.END_OBJECT)");
        checkResult(deserializeImpl, varName);
        deserializeImpl.addStatement("return $L", varName);

        deserializerBuilder.addMethod(deserializeImpl.build());
    }

    private void checkResult(MethodSpec.Builder deserializeImpl, String varName) {
        currentDeserInfo.getUnit().getProps().forEach(prop -> {
            String condition;
            String accessor;
            if (prop.getTName().isPrimitive()) {
                accessor = presenceFlag(prop.getName());
                condition = "!" + accessor;
            } else {
                accessor = prop.getAccessor(varName);
                condition = accessor + " == null";
            }

            deserializeImpl.addStatement("if ($L) throw new IllegalStateException(\"Missing field: \" + $L)",
                    condition, convertToSerializedConstName(prop.getName()));
        });
    }

    private String instantiate(ClassName beanClass, MethodSpec.Builder deserializeImpl) {
        String varName = "_" + currentDeserInfo.getUnit().getSimpleName().toLowerCase();

        deserializeImpl.addStatement("$1T $2L = new $1T()", beanClass, varName);
        return varName;
    }

    private void genCommonParsing(MethodSpec.Builder deserializeImpl, String varName) throws GenerationException {
        deserializeImpl
                .beginControlFlow("for (; parser.getCurrentToken() == $T.FIELD_NAME; parser.nextToken())", ClassName.get(JsonToken.class))
                .addStatement("String field = parser.getCurrentName()")
                .addStatement("Integer I = fullFieldToIndex.get(field)")
                .beginControlFlow("if (I != null)")
                .beginControlFlow("switch (I)");

        for (Property property : currentDeserInfo.getUnit().getProps()) {
            deserializeImpl.addCode("case $L:\n$>", convertToIndexConstName(property.getName()));
            deserializeImpl.addStatement("parser.nextToken()");
            String propVarName = addPropertyReading(deserializeImpl, property);
            assignObjectsProperty(deserializeImpl, varName, property, propVarName, true);
            deserializeImpl.addStatement("continue$<");

        }

        deserializeImpl
                .endControlFlow()
                .endControlFlow()
                .addStatement("throw new IllegalStateException(\"Unexpected field '\"+field+\"'\")")
                .endControlFlow();
    }


    private void genFastParsing(MethodSpec.Builder deserializeImpl, String varName, int propIndex) throws GenerationException {
        Property property = currentDeserInfo.getUnit().getProps().get(propIndex);

        deserializeImpl
                .beginControlFlow("if (parser.nextFieldName($L))", convertToSerializedConstName(property.getName()));

        deserializeImpl.addStatement("parser.nextToken()");
        String propVarName = addPropertyReading(deserializeImpl, property);
        assignObjectsProperty(deserializeImpl, varName, property, propVarName, true);

        if (propIndex + 1 < currentDeserInfo.getUnit().getProps().size()) {
            genFastParsing(deserializeImpl, varName, propIndex + 1);
        } else {
            deserializeImpl
                    .addStatement("parser.nextToken()")
                    .addStatement("verifyCurrent(parser, $T.END_OBJECT)", ClassName.get(JsonToken.class))
                    .addStatement("return $L", varName);
        }

        deserializeImpl.endControlFlow();
    }

    /**
     * @param readMethod - method to which property reading code will be pasted
     * @param property to read
     * @return name of variable in which given property is read.
     */
    private String addPropertyReading(MethodSpec.Builder readMethod, Property property) throws GenerationException {

        String propVarName = property.getName() + "_read";

        readMethod.addCode("$1T $2L = parser.currentToken() == JsonToken.VALUE_NULL ? null : ", property.getTName(), propVarName);

        if (property instanceof EnumProp) {
            readMethod.addStatement("$1T.valueOf($2L)", property.getTName(), property.parseMethod("parser"));
        } else if (property.isSimple()) {
            readMethod.addStatement("$L", property.parseMethod("parser"));
        } else if (property instanceof ContainerProp) {
            MethodSpec arrayRead = containerRead((ContainerProp) property);
            readMethod.addStatement("$N(parser, ctxt)", arrayRead);
            currentDeserInfo.getReadMethods().put(arrayRead.name, arrayRead);
        } else if (property instanceof MapProp) {
            MethodSpec mapRead = mapRead((MapProp) property);
            readMethod.addStatement("$N(parser, ctxt)", mapRead);
            currentDeserInfo.getReadMethods().put(mapRead.name, mapRead);
        } else if (property.getOldProperty() != null) {
            MethodSpec withOldTypeRead = withOldTypeRead(property);
            readMethod.addStatement("$N(parser, ctxt)", withOldTypeRead);
            currentDeserInfo.getReadMethods().put(withOldTypeRead.name, withOldTypeRead);
        } else {
            readMethod.addStatement("($1T) $2L.deserialize(parser, ctxt)",
                                    property.getTName(), deserializerName(property.getTName()));
            currentDeserInfo.getProvided().add(property.getTName());
        }

        return propVarName;
    }

    private MethodSpec withOldTypeRead(Property property) throws GenerationException {
        MethodSpec.Builder builder = MethodSpec
                .methodBuilder("read_" + property.getName())
                .addModifiers(Modifier.PRIVATE)
                .returns(property.getTName())
                .addParameter(JsonParser.class, "parser")
                .addParameter(DeserializationContext.class, "ctxt")
                .addException(IOException.class);


        builder
                .beginControlFlow("if (parser.currentToken() != JsonToken.START_OBJECT)")
                .addStatement("$1T ret = new $1T()", property.getTName());

        BeanDescription description = Inspector.getDescription(property.getTypeName())
                .orElseThrow(() -> new GenerationException("Couldn't access type information for " + property.getTypeName()));

        Property oldProperty = description.getProps().stream()
                .filter(prop -> prop.getName().equals(property.getOldProperty()))
                .findAny()
                .orElseThrow(() -> new GenerationException(
                        "Couldn't find " + property.getOldProperty() + " property inside " + property.getName()));
        String var = addPropertyReading(builder, oldProperty);
        assignObjectsProperty(builder, "ret", oldProperty, var, false);
        builder
                .addStatement("return ret")
                .nextControlFlow("else");
        property.setOldProperty(null);
        String propVarName = addPropertyReading(builder, property);
        builder.addStatement("return $L", propVarName);
        builder.endControlFlow();
        return builder.build();
    }

    /**
     *
     * @param readMethod method to insert code in
     * @param property property to assign
     * @param propVarName name of variable with property value
     * @param presenceFlagAssign true if property presence flag must be assigned too.
     */
    private void assignObjectsProperty(MethodSpec.Builder readMethod, String objVarName,
                                       Property property, String propVarName, boolean presenceFlagAssign) {
        if (property.getSetter() == null) {
            readMethod.addStatement("$L = $L", property.getAccessor(objVarName), propVarName);
        } else {
            readMethod.addStatement("$L.$L($L)", objVarName, property.getSetter(), propVarName);
        }

        if (property.getTName().isPrimitive() && presenceFlagAssign) {
            readMethod.addStatement("$L = true", presenceFlag(property.getName()));
        }
    }

    private MethodSpec mapRead(MapProp property) throws GenerationException {
        MethodSpec.Builder builder = MethodSpec
                .methodBuilder("read_map_" + property.getName())
                .addModifiers(Modifier.PRIVATE)
                .returns(property.getTName())
                .addParameter(JsonParser.class, "parser")
                .addParameter(DeserializationContext.class, "ctxt")
                .addException(IOException.class)
                .addStatement("verifyCurrent(parser, JsonToken.START_OBJECT)");

        //fixme: here is assumption that if map class is not java.util.Map then it is not abstract
        TypeName inst = Utils.erasure(property.getTypeName()).equals("java.util.Map")
                ? ParameterizedTypeName.get(ClassName.get(HashMap.class), property.getKey().getTName(), property.getValue().getTName())
                : property.getTName();

        builder
                .addStatement("$T res = new $T()", property.getTName(), inst)
                .addStatement("String keyStr = parser.nextFieldName()")
                .beginControlFlow(" for (; keyStr != null; keyStr = parser.nextFieldName())")
                .addStatement("$T key = $L", property.getKey().getTName(), property.getKey().parseMethod("keyStr"))
                .addStatement("JsonToken t = parser.nextToken()")
                .addStatement("$T value", property.getValue().getTName())
                .beginControlFlow("if (t == JsonToken.VALUE_NULL)")
                .addStatement("value = null")
                .nextControlFlow("else");

        String var = addPropertyReading(builder, property.getValue());

        builder
                .addStatement("value = $L", var)
                .endControlFlow()
                .addStatement("res.put(key, value)")
                .endControlFlow()
                .addStatement("return res");

        return builder.build();
    }

    private MethodSpec containerRead(ContainerProp property) throws GenerationException {
        MethodSpec.Builder builder = MethodSpec
                .methodBuilder("read_container_" + property.getName())
                .addModifiers(Modifier.PRIVATE)
                .returns(property.getTName())
                .addParameter(JsonParser.class, "parser")
                .addParameter(DeserializationContext.class, "ctxt")
                .addException(IOException.class)
                .beginControlFlow("if (parser.currentToken() != JsonToken.START_ARRAY)")
                .addStatement("reportIllegal(parser, JsonToken.START_ARRAY)")
                .endControlFlow();

        if (property instanceof ArrayProp) {
           addArrayReading(builder, (ArrayProp) property);
        } else {
            builder.addStatement("$1T arr = new $2T<>()", property.getTName(), ClassName.get(ArrayList.class));
            builder.beginControlFlow("while (parser.nextToken() != JsonToken.END_ARRAY)");
            String propVarName = addPropertyReading(builder, property.getElement());
            builder
                    .addStatement("arr.add($L)", propVarName)
                    .endControlFlow()
                    .addStatement("verifyCurrent(parser, JsonToken.END_ARRAY)")
                    .addStatement("return arr");
        }

        return builder.build();
    }

    private void addArrayReading(MethodSpec.Builder builder, ArrayProp property) {
        currentDeserInfo.getProvidedArrays().add(property);

        builder.addStatement("$1T arr = ($1T) $2L.deserialize(parser, ctxt)",
                                property.getTName(), deserializerName(property.getTName()))
                .addStatement("return arr");
    }


    private void addConstructors(TypeSpec.Builder deserializerBuilder ,ClassName beanClass) {
        ParameterizedTypeName classBean = ParameterizedTypeName.get(ClassName.get(Class.class), beanClass);
        MethodSpec constrOneArg = MethodSpec.constructorBuilder()
                .addParameter(ParameterSpec.builder(classBean, "t").build())
                .addStatement("super(t)")
                .addModifiers(Modifier.PROTECTED)
                .build();

        MethodSpec constrDef = MethodSpec.constructorBuilder()
                .addStatement("this($T.class)",  currentDeserInfo.getUnit().getClassName())
                .addModifiers(Modifier.PUBLIC)
                .build();

        deserializerBuilder
                .addMethod(constrOneArg)
                .addMethod(constrDef);
    }


    private void addResolve(TypeSpec.Builder deserializerBuilder) {
        MethodSpec.Builder resolve = MethodSpec.methodBuilder("resolve")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(DeserializationContext.class, "ctxt")
                .returns(void.class)
                .addException(JsonMappingException.class)
                .addStatement("$T javaType", ClassName.get(JavaType.class))
                .addStatement("$1T typeFactory = $1T.defaultInstance()", ClassName.get(TypeFactory.class));

        ClassName deserClass = ClassName.get(JsonDeserializer.class);
        currentDeserInfo.getProvided().forEach((typeName) -> {
            resolve.addStatement("javaType = typeFactory.constructType(new $T<$L>(){})",
                        ClassName.get(TypeReference.class), typeName);
            resolve.addStatement("$L = ctxt.findNonContextualValueDeserializer(javaType)", deserializerName(typeName));

            ParameterizedTypeName deserType = ParameterizedTypeName.get(deserClass, TypeName.get(Object.class));
            FieldSpec deserializer = FieldSpec.builder(deserType, deserializerName(typeName))
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            deserializerBuilder.addField(deserializer);
        });

        resolve.addStatement("$T arrayType", ClassName.get(ArrayType.class));

        currentDeserInfo.getProvidedArrays().forEach(arrayProp ->{
            TypeName deserType;
            if (arrayProp.isPrimitiveArray()) {
                deserType = ParameterizedTypeName.get(deserClass, arrayProp.getTName());
                resolve.addStatement("$L = (JsonDeserializer<$T>) $T.forType($T.class)",
                        deserializerName(arrayProp.getTName()), arrayProp.getTName(), ClassName.get(PrimitiveArrayDeserializers.class), arrayProp.getElement().getTName());
            } else {
                deserType = TypeName.get(ObjectArrayDeserializer.class);
                resolve.addStatement("arrayType = typeFactory.constructArrayType($L.class)", arrayProp.getElement().getTName());

                TypeSpec typeSpec = TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(ParameterizedTypeName.get(JsonDeserializer.class, Object.class))
                        .addMethod(MethodSpec.methodBuilder("deserialize")
                                .addParameter(JsonParser.class, "parser")
                                .addParameter(DeserializationContext.class, "ctxt")
                                .addException(IOException.class)
                                .returns(arrayProp.getElement().getTName())
                                .addModifiers(Modifier.PUBLIC)
                                .addStatement("return $T.valueOf(parser.getText())", arrayProp.getElement().getTName())
                                .build())
                        .build();

                resolve.addStatement("$L = new $T(arrayType, $L, null)",
                        deserializerName(arrayProp.getTName()), TypeName.get(ObjectArrayDeserializer.class),
                        arrayProp.getElement() instanceof EnumProp
                                ? typeSpec
                                : deserializerName(arrayProp.getElement().getTName()));
            }

            FieldSpec deserializer = FieldSpec.builder(deserType, deserializerName(arrayProp.getTName()))
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            deserializerBuilder.addField(deserializer);
        });

        deserializerBuilder.addMethod(resolve.build());


    }

    public static String getPackageName(BeanDescription unit) {
        return "com.vshatrov" + PACKAGE_MODIFIER;
        //return unit.getPackageName() + PACKAGE_MODIFIER;
    }

    private String presenceFlag(String name) {
        return "have_" + name;
    }

    private String deserializerName(TypeName typeName) {
       String res;
       String simple = Utils.qualifiedToSimple(typeName.toString());
       res = Character.toLowerCase(simple.charAt(0)) + simple.substring(1) + "_deserializer";
       return typeName instanceof ArrayTypeName ? "ar_" + res : res;
    }

    private String readMethodName(String name) {
        return "read_"+name.toLowerCase();
    }

    private String convertToStringAlternativeName(String mainName, String alternativeName, int i) {
        return convertToStringConstName(mainName) + "_ALT_" + i + "_" + alternativeName.toUpperCase();
    }

    private String convertToStringConstName(String name) {
        return "FULL_FIELD_" + name.toUpperCase();
    }

    private String convertToIndexConstName(String name) {
        return "IX_FULL_FIELD_" + name.toUpperCase();
    }

    private String convertToSerializedConstName(String name) {
        return "FIELD_" + name.toUpperCase();
    }

}
