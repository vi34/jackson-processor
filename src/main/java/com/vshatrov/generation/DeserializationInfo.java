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
 * @author Viktor Shatrov.
 */
public class DeserializationInfo {
    private String typeName;
    private Map<String, DeserializationInfo> props;


    private BeanDescription unit;
    private Map<String, Property> primitiveProps;
    private Map<String, MethodSpec> readMethods;
    private Set<TypeName> provided;
    private Set<ArrayProp> providedArrays;

    private JavaFile javaFile;

    public DeserializationInfo(BeanDescription unit) {
        this.typeName = unit.getTypeName();
        props = new HashMap<>();
        primitiveProps = unit.getProps().stream()
                .filter(property -> property.getTName().isPrimitive())
                .collect(Collectors.toMap(Property::getName, Function.identity()));
        this.unit = unit;
        readMethods = new HashMap<>();
        provided = new HashSet<>();
        providedArrays = new HashSet<>();
    }

    public String getTypeName() {
        return this.typeName;
    }

    public Map<String, DeserializationInfo> getProps() {
        return this.props;
    }

    public BeanDescription getUnit() {
        return this.unit;
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

    public JavaFile getJavaFile() {
        return this.javaFile;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setProps(Map<String, DeserializationInfo> props) {
        this.props = props;
    }

    public void setUnit(BeanDescription unit) {
        this.unit = unit;
    }

    public void setPrimitiveProps(Map<String, Property> primitiveProps) {
        this.primitiveProps = primitiveProps;
    }

    public void setReadMethods(Map<String, MethodSpec> readMethods) {
        this.readMethods = readMethods;
    }

    public void setProvided(Set<TypeName> provided) {
        this.provided = provided;
    }

    public void setProvidedArrays(Set<ArrayProp> providedArrays) {
        this.providedArrays = providedArrays;
    }

    public void setJavaFile(JavaFile javaFile) {
        this.javaFile = javaFile;
    }
}
