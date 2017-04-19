package com.vi34.beans;

import com.sun.tools.javac.code.Type;
import com.vi34.schema.JsonType;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;

import static com.vi34.utils.Utils.*;
import static javax.lang.model.type.TypeKind.*;

/**
 * Created by vi34 on 19/04/2017.
 */
public class PropertyFabric {

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
        property.setSimple(computeSimple(type));
        switch (property.getTypeName()) {
            case "boolean":case "java.lang.Boolean":
                property.jsonType = JsonType.BOOLEAN; break;
            case "char":case "java.lang.Character":case "java.lang.String":
                property.jsonType = JsonType.STRING; break;
            default:
                if (isNumber(type)) {
                    property.jsonType = JsonType.NUMBER;
                }
        }
    }

    private boolean computeSimple(TypeMirror type) {
        return type.getKind().isPrimitive() || isNumber(type) || isEnum(type)
                || type.toString().equals("java.lang.String")
                || type.toString().equals("java.lang.Character")
                || type.toString().equals("java.lang.Boolean");
    }


}
