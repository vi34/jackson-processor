package com.vshatrov.beans.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author Viktor Shatrov.
 */
@Getter
public class ArrayProp extends ContainerProp {

    private boolean isPrimitiveArray;

    ArrayProp(VariableElement element, Property elem) {
        super(element, elem);
        isPrimitiveArray = elem.typeKind.isPrimitive();
    }

    ArrayProp(TypeMirror type, Property elem) {
        super(type, elem);
        isPrimitiveArray = elem.typeKind.isPrimitive();
    }
}
