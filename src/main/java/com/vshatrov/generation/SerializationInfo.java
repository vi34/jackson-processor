package com.vshatrov.generation;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.vshatrov.beans.BeanDescription;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Viktor Shatrov.
 */
public class SerializationInfo {
    private String typeName;
    private MethodSpec serializeMethod;
    private Map<String, String> strings;
    private BeanDescription unit;

    private JavaFile serializerFile;
    /**
     *  type serializers which will be resolved at runtime
     */
    Set<TypeName> provided;

    public SerializationInfo(BeanDescription unit) {
        this.unit = unit;
        this.typeName = unit.getTypeName();
        strings = new HashMap<>();
        provided = new HashSet<>();
    }

    public String getTypeName() {
        return this.typeName;
    }

    public BeanDescription getUnit() {
        return unit;
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
