package com.vi34.beans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.lang.model.element.VariableElement;

/**
 * Created by vi34 on 03/04/2017.
 */
@Getter
@Setter
@ToString
public class Property {

    boolean isField;
    boolean isSimple; //Todo handle arrays and collections
    boolean isNumber;
    String name;
    String typeName;

    Property(VariableElement element) {
        name = element.getSimpleName().toString();
        typeName = element.asType().toString();
    }

    public String getter() {
        return "get" + Character.isUpperCase(name.charAt(0)) + name.substring(1) + "()";
    }
}
