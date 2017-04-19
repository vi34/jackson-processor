package com.vi34.beans;

import com.vi34.schema.JsonType;
import lombok.Getter;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

/**
 * Created by vi34 on 06/04/2017.
 */
@Getter
public class ContainerProp extends Property {

    Property elem;

    ContainerProp(VariableElement element, Property elem) {
        super(element);
        this.elem = elem;
        elem.accessor = Function.identity();
        jsonType = JsonType.ARRAY;
    }
}
