package com.vshatrov.generation;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.vshatrov.beans.BeanDescription;
import com.vshatrov.beans.properties.ArrayProp;
import com.vshatrov.beans.properties.Property;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Used as internal state during generation in {@link DeserializerGenerator}
 * @author Viktor Shatrov.
 */
public class DeserializationInfo extends GenerationInfo {
    private Map<String, DeserializationInfo> props;

    private Map<String, Property> primitiveProps;
    private Map<String, MethodSpec> readMethods;
    private Set<TypeName> provided;
    private Set<ArrayProp> providedArrays;

    public DeserializationInfo(BeanDescription unit) {
        super(unit);
        props = new HashMap<>();
        primitiveProps = unit.getProps().stream()
                .filter(property -> property.getTName().isPrimitive())
                .collect(Collectors.toMap(Property::getName, Function.identity()));
        readMethods = new HashMap<>();
        provided = new HashSet<>();
        providedArrays = new HashSet<>();
    }

    public Map<String, DeserializationInfo> getProps() {
        return this.props;
    }

    public Map<String, Property> getPrimitiveProps() {
        return this.primitiveProps;
    }

    public Map<String, MethodSpec> getReadMethods() {
        return this.readMethods;
    }

    public Set<TypeName> getProvided() {
        return this.provided;
    }

    public Set<ArrayProp> getProvidedArrays() {
        return this.providedArrays;
    }

}
