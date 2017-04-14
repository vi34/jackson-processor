package com.vi34.beans;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.vi34.utils.Utils;
import lombok.*;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by vi34 on 03/04/2017.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Property {

    boolean isField;
    boolean isSimple; //Todo handle arrays and collections
    boolean isNumber;
    boolean isEnum;
    String name;
    String typeName;
    String accessor;
    TypeName tName;

    Property(VariableElement element) {
        name = element.getSimpleName().toString();
        typeName = element.asType().toString();
        tName = TypeName.get(element.asType());
    }

    Property(TypeMirror type) {
        typeName = type.toString();
        name = Utils.qualifiedToSimple(typeName);
        tName = TypeName.get(type);
    }

    public String getter() {
        return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1) + "()";
    }
}
