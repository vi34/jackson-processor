package com.vi34.beans;

import lombok.Getter;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

/**
 * Created by vi34 on 03/04/2017.
 */
@Getter
public class Property {

    boolean isField;
    String name;
    TypeKind kind;

    Property(VariableElement element) {
        isField = true;
        name = element.getSimpleName().toString();
        kind = element.asType().getKind();
    }

    public String accessorString() {
        return isField ? name : getter(name);
    }

    public String genMethod() {
        String genMethod;
        switch (kind) {
            case BOOLEAN: genMethod = "writeBooleanField"; break;
            case DOUBLE: case INT: genMethod = "writeNumberField"; break;
            default: //throw exception
                genMethod = null;
        }
        return genMethod;
    }

    private String getter(String name) {
        return "get" + Character.isUpperCase(name.charAt(0)) + name.substring(1) + "()";
    }
}
