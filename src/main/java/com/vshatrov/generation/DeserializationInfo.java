package com.vshatrov.generation;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.vshatrov.beans.BeanDescription;
import com.vshatrov.beans.properties.ArrayProp;
import com.vshatrov.beans.properties.MapProp;
import com.vshatrov.beans.properties.Property;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Viktor Shatrov.
 */
@Getter
@Setter
public class DeserializationInfo {
    String typeName;
    MethodSpec deserializeMethod;
    Map<String, DeserializationInfo> props;


    private BeanDescription unit;
    private Map<String, Property> primitiveProps;
    private Map<String, MethodSpec> readMethods;
    private Set<Property> provided;
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
}
