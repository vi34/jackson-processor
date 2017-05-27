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
 * Used as internal state during generation in {@link SerializerGenerator}
 * @author Viktor Shatrov.
 */
public class SerializationInfo extends GenerationInfo {
    private Map<String, String> strings;

    /**
     *  type serializers which will be resolved at runtime
     */
    Set<TypeName> provided;

    public SerializationInfo(BeanDescription unit) {
        super(unit);
        strings = new HashMap<>();
        provided = new HashSet<>();
    }

    public Map<String, String> getStrings() {
        return this.strings;
    }

    public Set<TypeName> getProvided() {
        return this.provided;
    }
}
