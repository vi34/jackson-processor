package com.vi34.beans;

import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vi34 on 22/12/2016.
 */
@Getter
@Setter
public class BeanDescription {

    String simpleName;
    String packageName;
    String typeName;
    TypeMirror type;
    TypeElement element;
    List<Property> props;
    //TODO: save getters for fields


    public BeanDescription(TypeElement element) {
        this.element = element;
        simpleName = element.getSimpleName().toString();
        String qualified = element.getQualifiedName().toString();
        int dot = qualified.lastIndexOf(".");
        packageName = dot > 0 ? qualified.substring(0, dot) : "";
        props = new ArrayList<>();
        type = element.asType();
        typeName = type.toString();
    }


}
