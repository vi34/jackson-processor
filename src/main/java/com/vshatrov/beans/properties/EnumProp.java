package com.vshatrov.beans.properties;

import com.vshatrov.GenerationException;
import com.vshatrov.schema.JsonType;
import lombok.EqualsAndHashCode;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author Viktor Shatrov.
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


    @Override
    public String parseMethod(String parser) throws GenerationException {
        return parser + ".getText()";
    }
}
