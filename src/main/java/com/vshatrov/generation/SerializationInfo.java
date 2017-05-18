package com.vshatrov.generation;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Viktor Shatrov.
 */
public class SerializationInfo {
    String typeName;
    MethodSpec serializeMethod;
    Map<String, String> strings;


    JavaFile serializerFile;
    /**
     *  type serializers which will be resolved at runtime
     */
    Set<TypeName> provided;

    public SerializationInfo(String typeName) {
        this.typeName = typeName;
        strings = new HashMap<>();
        provided = new HashSet<>();
    }

    public String getTypeName() {
        return this.typeName;
    }

    public MethodSpec getSerializeMethod() {
        return this.serializeMethod;
    }

    public Map<String, String> getStrings() {
        return this.strings;
    }

    public JavaFile getSerializerFile() {
        return this.serializerFile;
    }

    public Set<TypeName> getProvided() {
        return this.provided;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setSerializeMethod(MethodSpec serializeMethod) {
        this.serializeMethod = serializeMethod;
    }

    public void setStrings(Map<String, String> strings) {
        this.strings = strings;
    }

    public void setSerializerFile(JavaFile serializerFile) {
        this.serializerFile = serializerFile;
    }

    public void setProvided(Set<TypeName> provided) {
        this.provided = provided;
    }
}
