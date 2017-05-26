package com.vshatrov.beans.properties;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.vshatrov.utils.Utils;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Viktor Shatrov.
 */
public class ListProp extends ContainerProp {

    ListProp(VariableElement element, Property elem) {
        super(element, elem);
    }

    ListProp(TypeMirror type, Property elem) {
        super(type, elem);
    }

    @Override
    public CodeBlock defaultInstance() {
        if (isInterface) {
            ParameterizedTypeName type = ParameterizedTypeName.get(ClassName.get(ArrayList.class), element.getTName());
            return CodeBlock.builder()
                    .add("new $T()", type)
                    .add(";\n$]")
                    .build();
        } else {
            return super.defaultInstance();
        }
    }
}
