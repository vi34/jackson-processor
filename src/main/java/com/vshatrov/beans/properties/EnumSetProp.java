package com.vshatrov.beans.properties;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.EnumSet;
import java.util.HashSet;

/**
 * @author Viktor Shatrov.
 */
public class EnumSetProp extends ContainerProp {

    EnumSetProp(VariableElement element, Property elem) {
        super(element, elem);
    }

    EnumSetProp(TypeMirror type, Property elem) {
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
