package com.vshatrov.processor.generation;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.vshatrov.processor.utils.Utils.filer;

/**
 * Class generates needed information for correct work of {@link com.vshatrov.processor.APTModule}.
 * Generated class contains information about all generated serializers and deserializers.
 * @author Viktor Shatrov.
 */
public class ModuleInfoGenerator {
    private Map<String, DeserializationInfo> processedDeserializers;
    private Map<String, SerializationInfo> processedSerializers;

    public ModuleInfoGenerator(Map<String, DeserializationInfo> processedDeserializers, Map<String, SerializationInfo> processedSerializers) {
        this.processedDeserializers = processedDeserializers;
        this.processedSerializers = processedSerializers;
    }

    public void generateModuleInfo() throws IOException {
        TypeSpec.Builder module = TypeSpec.classBuilder("ModuleInfo")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        MethodSpec.Builder serializers = MethodSpec.methodBuilder("serializers")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(String[].class)
                .addCode("$[return new String[]{");

        addGeneratedInfo(serializers, new ArrayList<>(processedSerializers.values()));
        module.addMethod(serializers.build());
        MethodSpec.Builder deserializers = MethodSpec.methodBuilder("deserializers")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(String[].class)
                .addCode("$[return new String[]{");

        addGeneratedInfo(deserializers, new ArrayList<>(processedDeserializers.values()));
        module.addMethod(deserializers.build());

        JavaFile javaFile = JavaFile.builder("com.vshatrov.generated", module.build())
                .indent("    ")
                .build();

        javaFile.writeTo(filer);

    }

    private void addGeneratedInfo(MethodSpec.Builder method, List<GenerationInfo> infos) {
        GenerationInfo ser = infos.get(0);
        JavaFile serializerFile = ser.getJavaFile();
        method.addCode("$S", serializerFile.packageName + "." +
                serializerFile.typeSpec.name + ":" + ser.getTypeName());

        for (int i = 1; i < infos.size(); i++) {
            ser = infos.get(i);
            serializerFile = ser.getJavaFile();
            method.addCode(", $S", serializerFile.packageName + "." +
                    serializerFile.typeSpec.name + ":" + ser.getTypeName());
        }
        method.addCode("};\n$]");
    }
}
