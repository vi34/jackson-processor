package com.vshatrov.processor.type.properties;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author Viktor Shatrov.
 */
public class ArrayProperty extends ContainerProperty {

    private boolean isPrimitiveArray;

    ArrayProperty(VariableElement element, Property elem) {
        super(element, elem);
        isPrimitiveArray = elem.typeKind.isPrimitive();
    }

    ArrayProperty(TypeMirror type, Property elem) {
        super(type, elem);
        isPrimitiveArray = elem.typeKind.isPrimitive();
    }

    public boolean isPrimitiveArray() {
        return this.isPrimitiveArray;
    }
}
