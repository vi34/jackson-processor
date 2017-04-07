package com.vi34.beans;

import com.squareup.javapoet.MethodSpec;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vi34 on 06/04/2017.
 */
@Getter
@Setter
public class SerializeInfo {
    String typeName;
    MethodSpec serializeMethod;
    Map<String, SerializeInfo> props;

    public SerializeInfo(String typeName) {
        this.typeName = typeName;
        props = new HashMap<>();
    }
}
