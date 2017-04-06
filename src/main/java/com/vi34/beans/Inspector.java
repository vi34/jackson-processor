package com.vi34.beans;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;

import static javax.lang.model.type.TypeKind.*;

/**
 * Created by vi34 on 18/02/2017.
 */
public class Inspector {
    private Elements elementUtils;
    private Types typeUtils;

    public Inspector(Elements elementUtils, Types typeUtils) {
        this.elementUtils = elementUtils;
        this.typeUtils = typeUtils;
    }

    public BeanDefinition inspect(TypeElement element) {
        List<? extends Element> members = elementUtils.getAllMembers(element);
        BeanDefinition definition = new BeanDefinition(element);
        for (Element member: members) {
            if (member.getKind().equals(ElementKind.FIELD)) {
                processField(definition, (VariableElement) member);
            }
            //TODO add getters/setters
        }
        return definition;
    }

    private void processField(BeanDefinition definition, VariableElement member) {
        if (!member.getModifiers().contains(Modifier.PRIVATE)) {
            Property property = new Property(member);
            property.setField(true);
            property.setNumber(isNumber(member));
            property.setSimple(computeSimple(member, property));
            definition.getProps().add(property);
        }
    }

    private boolean computeSimple(VariableElement element, Property prop) {
        return element.asType().getKind().isPrimitive() || prop.isNumber()
                || prop.getTypeName().equals("java.lang.String")
                || prop.getTypeName().equals("java.lang.Character")
                || prop.getTypeName().equals("java.lang.Boolean");
    }

    private boolean isNumber(VariableElement element) {
        List<? extends TypeMirror> supertypes = typeUtils.directSupertypes(element.asType());
        if (supertypes.size() > 0 && supertypes.get(0).toString().equals("java.lang.Number"))
            return true;

        TypeKind kind = element.asType().getKind();
        return kind == INT || kind == LONG || kind == SHORT || kind == BYTE || kind == DOUBLE || kind == FLOAT;

    }

}
