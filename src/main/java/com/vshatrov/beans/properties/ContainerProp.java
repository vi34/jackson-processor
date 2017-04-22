package com.vshatrov.beans.properties;

import com.vshatrov.schema.JsonType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

/**
 * @author Viktor Shatrov.
 */
@Getter
public class ContainerProp extends Property {

    Property elem;

    ContainerProp(VariableElement element, Property elem) {
        super(element);
        this.elem = elem;
        elem.dynamicAccessor = Function.identity();
        jsonType = JsonType.ARRAY;
    }

    ContainerProp(TypeMirror type, Property elem) {
        super(type);
        this.elem = elem;
        elem.dynamicAccessor = Function.identity();
        jsonType = JsonType.ARRAY;
    }
}
