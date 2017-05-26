package com.vshatrov.beans.properties;

import com.vshatrov.schema.JsonType;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

/**
 * Properties which usually serialized as array in json.
 * @author Viktor Shatrov.
 */
public class ContainerProp extends Property {

    Property element;

    ContainerProp(VariableElement element, Property elem) {
        super(element);
        this.element = elem;
        elem.dynamicAccessor = Function.identity();
        jsonType = JsonType.ARRAY;
    }

    ContainerProp(TypeMirror type, Property element) {
        super(type);
        this.element = element;
        element.dynamicAccessor = Function.identity();
        jsonType = JsonType.ARRAY;
    }

    public Property getElement() {
        return this.element;
    }
}
