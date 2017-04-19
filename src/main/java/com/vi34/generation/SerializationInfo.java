package com.vi34.generation;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.vi34.beans.Property;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vi34 on 06/04/2017.
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
    Map<String, TypeName> provided;

    public SerializationInfo(String typeName) {
        this.typeName = typeName;
        props = new HashMap<>();
        strings = new HashMap<>();
        provided = new HashMap<>();
    }
}
