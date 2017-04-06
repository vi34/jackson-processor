package com.vi34.beans;

import com.squareup.javapoet.MethodSpec;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vi34 on 06/04/2017.
 */
@Getter
@Setter
public class SerializeInfo {
    String typeName;
    MethodSpec serializeMethod;
    List<SerializeInfo> props;

    public SerializeInfo(String typeName) {
        this.typeName = typeName;
        props = new ArrayList<>();
    }
}
