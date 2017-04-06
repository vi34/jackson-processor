package com.vi34.beans;

import com.squareup.javapoet.MethodSpec;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Created by vi34 on 03/04/2017.
 */
@Getter
@Setter
public class Property {

    boolean isField;
    boolean isDeclared;
    String name;
    String typeName;
    String genMethod;

    Property(VariableElement element) {
        isField = true;
        name = element.getSimpleName().toString();
        isDeclared = !element.asType().getKind().isPrimitive(); // TODO: &&  not string
        typeName = element.asType().toString();
        genMethod(element.asType());
    }

    public String accessorString() {
        return isField ? name : getter(name);
    }

    public void genMethod(TypeMirror type) {
        switch (type.getKind()) {
            case BOOLEAN: genMethod = "writeBoolean"; break;
            case DOUBLE: case INT: genMethod = "writeNumber"; break;
            default: //throw exception

                genMethod = null;
        }
    }

    private String getter(String name) {
        return "get" + Character.isUpperCase(name.charAt(0)) + name.substring(1) + "()";
    }
}
