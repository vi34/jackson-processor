package com.vshatrov.beans.properties;

import com.sun.tools.javac.code.Type;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;

import static com.vshatrov.utils.Utils.*;
import static javax.lang.model.type.TypeKind.*;

/**
 * @author Viktor Shatrov.
 */
public class PropertyFabric {

    public Property construct(VariableElement member) {
        Property property;
        TypeMirror type = member.asType();
        property = constructProp(member, type);
        return property;
    }

    private Property constructProp(VariableElement member, TypeMirror type) {
        Property property;
        if (iterable(type)) {
            TypeMirror elemType = iterable(type) ? ((Type.ClassType) type).getTypeArguments().get(0) : ((ArrayType) type).getComponentType();
            Property propertyEl = constructProp(null, elemType);
            property = member != null
                    ? new ContainerProp(member, propertyEl)
                    : new ContainerProp(type, propertyEl);
        } else if (type.getKind().equals(ARRAY)) {
            TypeMirror elemType = ((ArrayType) type).getComponentType();
            Property propertyEl = constructProp(null, elemType);
            property = member != null
                    ? new ArrayProp(member, propertyEl)
                    : new ArrayProp(type, propertyEl);
        } else if (isMap(type)) {
            TypeMirror keyType = ((Type.ClassType) type).getTypeArguments().get(0);
            TypeMirror valueType = ((Type.ClassType) type).getTypeArguments().get(1);
            MapProp.KeyProp keyProp = new MapProp.KeyProp(keyType);
            Property valueProp = constructProp(null, valueType);
            property = member != null
                    ? new MapProp(member, keyProp, valueProp)
                    : new MapProp(type, keyProp, valueProp);
        } else if (isEnum(type)) {
            property = member != null ? new EnumProp(member) : new EnumProp(type);
            property.setSimple(true);
        } else {
            property = member != null ? new Property(member) : new Property(type);
        }
        return property;
    }

}
