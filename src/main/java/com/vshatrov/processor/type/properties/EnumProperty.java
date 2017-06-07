package com.vshatrov.processor.type.properties;

import com.vshatrov.processor.GenerationException;
import com.vshatrov.processor.schema.JsonType;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author Viktor Shatrov.
 */
public class EnumProperty extends Property {

    EnumProperty(VariableElement element) {
        super(element);
        jsonType = JsonType.STRING;
    }

    EnumProperty(TypeMirror type) {
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
