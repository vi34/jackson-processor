package com.vi34.beans;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by vi34 on 15/04/2017.
 */
public class EnumProp extends Property {
    EnumProp(VariableElement element) {
        super(element);
    }

    EnumProp(TypeMirror type) {
        super(type);
    }

    @Override
    public String getAccessor(String var) {
        return var + "." + getter() + ".toString()";
    }
}
