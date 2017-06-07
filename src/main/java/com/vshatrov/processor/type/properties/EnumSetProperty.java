package com.vshatrov.processor.type.properties;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.EnumSet;

/**
 * @author Viktor Shatrov.
 */
public class EnumSetProperty extends ContainerProperty {

    EnumSetProperty(VariableElement element, Property elem) {
        super(element, elem);
    }

    EnumSetProperty(TypeMirror type, Property elem) {
        super(type, elem);
    }

    @Override
    public CodeBlock defaultInstance() {
        return CodeBlock.builder()
                .add("$T.noneOf($T.class)", EnumSet.class, element.getTName())
                .add(";\n$]")
                .build();
    }
}
