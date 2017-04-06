package com.vi34.beans;

import com.squareup.javapoet.TypeName;
import lombok.Getter;

import javax.lang.model.element.VariableElement;

/**
 * Created by vi34 on 06/04/2017.
 */
@Getter
public class ContainerProp extends Property {

    Property elem;

    ContainerProp(VariableElement element) {
        super(element);

    }
}
