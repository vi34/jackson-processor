package com.vi34.beans;

import com.vi34.GenerationException;
import com.vi34.schema.JsonType;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by vi34 on 15/04/2017.
 */
public class EnumProp extends Property {

    EnumProp(VariableElement element) {
        super(element);
        jsonType = JsonType.STRING;
    }

    EnumProp(TypeMirror type) {
        super(type);
        jsonType = JsonType.STRING;
    }

    @Override
    protected String modifyAccess(String accessor) {
        return accessor + ".toString()";
    }

}
