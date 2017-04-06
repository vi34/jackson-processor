package com.vi34.beans;

import javax.lang.model.element.*;
import javax.lang.model.type.ArrayType;
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
            TypeMirror type = member.asType();
            if (type.getKind().equals(ARRAY)) {// TODO add collections
                type = ((ArrayType) type).getComponentType();
                ContainerProp property = new ContainerProp(member);
                property.setField(true);
                Property propertyEl = new Property(type);
                fillWithType(type, propertyEl);
                property.elem = propertyEl;
                propertyEl.setAccessor(""+propertyEl.getName().toLowerCase().charAt(0));
                definition.getProps().add(property);
            } else {
                Property property = new Property(member);
                property.setField(true);
                fillWithType(type, property);
                definition.getProps().add(property);
            }
        }
    }

    private void fillWithType(TypeMirror type, Property property) {
        property.setNumber(isNumber(type));
        property.setSimple(computeSimple(type));
    }

    private boolean computeSimple(TypeMirror type) {
        return type.getKind().isPrimitive() || isNumber(type)
                || type.toString().equals("java.lang.String")
                || type.toString().equals("java.lang.Character")
                || type.toString().equals("java.lang.Boolean");
    }

    private boolean isNumber(TypeMirror type) {
        List<? extends TypeMirror> supertypes = typeUtils.directSupertypes(type);
        if (supertypes.size() > 0 && supertypes.get(0).toString().equals("java.lang.Number"))
            return true;

        TypeKind kind = type.getKind();
        return kind == INT || kind == LONG || kind == SHORT || kind == BYTE || kind == DOUBLE || kind == FLOAT;

    }

}
