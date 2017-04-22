package com.vshatrov.generation;

import com.squareup.javapoet.MethodSpec;
import com.vshatrov.beans.properties.Property;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author Viktor Shatrov.
 */
@Getter
@Setter
public class SerializationInfo {
    String typeName;
    MethodSpec serializeMethod;
    Map<String, SerializationInfo> props;
    Map<String, String> strings;

    /**
     *  properties which will be resolved at runtime
     */
    Set<Property> provided;

    public SerializationInfo(String typeName) {
        this.typeName = typeName;
        props = new HashMap<>();
        strings = new HashMap<>();
        provided = new HashSet<>();
    }
}
