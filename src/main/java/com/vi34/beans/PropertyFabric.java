package com.vi34.beans;

import com.sun.tools.javac.code.Type;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import java.util.List;

import static javax.lang.model.type.TypeKind.*;

/**
 * Created by vi34 on 19/04/2017.
 */
public class PropertyFabric {
    private Types typeUtils;

    PropertyFabric (Types typeUtils) {
        this.typeUtils = typeUtils;
    }

    Property construct(TypeMirror type) {
        Property property;
        if (isEnum(type)) {
            property = new EnumProp(type);
        } else {
            property = new Property(type);
        }
        fillWithType(type, property);
        return property;
    }

    Property construct(VariableElement member) {
        Property property;
        TypeMirror type = member.asType();
        if (type.getKind().equals(ARRAY) || iterable(type)) {
            type = iterable(type) ? ((Type.ClassType) type).getTypeArguments().get(0) : ((ArrayType) type).getComponentType();
            Property propertyEl = construct(type);
            fillWithType(type, propertyEl);
            property = new ContainerProp(member, propertyEl);
        } else if (isMap(type)) {
            TypeMirror keyType = ((Type.ClassType) type).getTypeArguments().get(0);
            TypeMirror valueType = ((Type.ClassType) type).getTypeArguments().get(1);
            MapProp.KeyProp keyProp = new MapProp.KeyProp(keyType);
            fillWithType(keyType, keyProp);
            Property valueProp = construct(valueType);
            property = new MapProp(member, keyProp, valueProp);
        } else if (isEnum(type)) {
            property = new EnumProp(member);
            property.setSimple(true);
        } else {
            property = new Property(member);
            fillWithType(type, property);
        }
        return property;
    }


    private void fillWithType(TypeMirror type, Property property) {
        property.setNumber(isNumber(type));
        property.setSimple(computeSimple(type));
    }

    private boolean isEnum(TypeMirror type) {
        return haveSupertype(type, "java.lang.Enum");
    }

    private boolean computeSimple(TypeMirror type) {
        return type.getKind().isPrimitive() || isNumber(type) || isEnum(type)
                || type.toString().equals("java.lang.String")
                || type.toString().equals("java.lang.Character")
                || type.toString().equals("java.lang.Boolean");
    }

    private boolean iterable(TypeMirror type) {
        return haveSupertype(type, "java.util.Collection");
    }

    private boolean isMap(TypeMirror type) {
        return haveSupertype(type, "java.util.Map");
    }


    private boolean haveSupertype(TypeMirror type, String superType) {
        if (typeUtils.erasure(type).toString().equals(superType)) return true;

        for (TypeMirror directSuperType : typeUtils.directSupertypes(type)) {
            if (haveSupertype(directSuperType, superType))
                return true;
        }
        return false;
    }

    private boolean isNumber(TypeMirror type) {
        if (haveSupertype(type, "java.lang.Number")) return true;

        TypeKind kind = type.getKind();
        return kind == INT || kind == LONG || kind == SHORT || kind == BYTE || kind == DOUBLE || kind == FLOAT;
    }
}
