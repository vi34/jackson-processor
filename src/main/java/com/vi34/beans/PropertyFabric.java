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

    Property construct(VariableElement member) {
        Property property;
        TypeMirror type = member.asType();
        property = constructProp(member, type);
        return property;
    }

    private Property constructProp(VariableElement member, TypeMirror type) {
        Property property;
        if (type.getKind().equals(ARRAY) || iterable(type)) {
            TypeMirror elemType = iterable(type) ? ((Type.ClassType) type).getTypeArguments().get(0) : ((ArrayType) type).getComponentType();
            Property propertyEl = constructProp(null, elemType);
            fillWithType(elemType, propertyEl);
            property = member != null
                    ? new ContainerProp(member, propertyEl)
                    : new ContainerProp(type, propertyEl);
        } else if (isMap(type)) {
            TypeMirror keyType = ((Type.ClassType) type).getTypeArguments().get(0);
            TypeMirror valueType = ((Type.ClassType) type).getTypeArguments().get(1);
            MapProp.KeyProp keyProp = new MapProp.KeyProp(keyType);
            fillWithType(keyType, keyProp);
            Property valueProp = constructProp(null, valueType);
            property = member != null
                    ? new MapProp(member, keyProp, valueProp)
                    : new MapProp(type, keyProp, valueProp);
        } else if (isEnum(type)) {
            property = member != null ? new EnumProp(member) : new EnumProp(type);
            property.setSimple(true);
        } else {
            property = member != null ? new Property(member) : new Property(type);
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
