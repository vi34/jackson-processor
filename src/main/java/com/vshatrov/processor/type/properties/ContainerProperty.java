package com.vshatrov.processor.type.properties;

import com.vshatrov.processor.schema.JsonType;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

/**
 * Properties which usually serialized as array in json.
 * @author Viktor Shatrov.
 */
public class ContainerProperty extends Property {

    Property element;

    ContainerProperty(VariableElement element, Property elem) {
        super(element);
        this.element = elem;
        elem.dynamicAccessor = Function.identity();
        jsonType = JsonType.ARRAY;
    }

    ContainerProperty(TypeMirror type, Property element) {
        super(type);
        this.element = element;
        element.dynamicAccessor = Function.identity();
        jsonType = JsonType.ARRAY;
    }

    public Property getElement() {
        return this.element;
    }
}
