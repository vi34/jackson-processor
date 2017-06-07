package com.vshatrov.processor.type.properties;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.HashSet;

/**
 * @author Viktor Shatrov.
 */
public class SetProperty extends ContainerProperty {

    SetProperty(VariableElement element, Property elem) {
        super(element, elem);
    }

    SetProperty(TypeMirror type, Property elem) {
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
