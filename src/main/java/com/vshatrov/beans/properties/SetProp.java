package com.vshatrov.beans.properties;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.vshatrov.utils.Utils;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author Viktor Shatrov.
 */
public class SetProp extends ContainerProp {

    SetProp(VariableElement element, Property elem) {
        super(element, elem);
    }

    SetProp(TypeMirror type, Property elem) {
        super(type, elem);
    }

    @Override
    public CodeBlock defaultInstance() {
        if (isInterface) {
            ParameterizedTypeName type = ParameterizedTypeName.get(ClassName.get(HashSet.class), element.getTName());
            return CodeBlock.builder()
                    .add("new $T()", type)
                    .add(";\n$]")
                    .build();
        } else {
            return super.defaultInstance();
        }
    }
}
