package com.vshatrov.processor.type.properties;

import com.sun.tools.javac.code.Type;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;

import static com.vshatrov.processor.utils.Utils.*;
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
        if (type.getKind().equals(ARRAY)) {
            TypeMirror elemType = ((ArrayType) type).getComponentType();
            Property propertyEl = constructProp(null, elemType);
            property = member != null
                    ? new ArrayProperty(member, propertyEl)
                    : new ArrayProperty(type, propertyEl);
        } else if (isMap(type)) {
            TypeMirror keyType = ((Type.ClassType) type).getTypeArguments().get(0);
            TypeMirror valueType = ((Type.ClassType) type).getTypeArguments().get(1);
            MapProperty.KeyProp keyProp = new MapProperty.KeyProp(keyType);
            Property valueProp = constructProp(null, valueType);
            property = member != null
                    ? new MapProperty(member, keyProp, valueProp)
                    : new MapProperty(type, keyProp, valueProp);
        } else if (isEnum(type)) {
            property = member != null ? new EnumProperty(member) : new EnumProperty(type);
            property.setSimple(true);
        } else if (isEnumSet(type)) {
            TypeMirror elemType = ((Type.ClassType) type).getTypeArguments().get(0);
            Property propertyEl = constructProp(null, elemType);
            property = member != null
                    ? new EnumSetProperty(member, propertyEl)
                    : new EnumSetProperty(type, propertyEl);
        } else if (isSet(type)) {
            TypeMirror elemType = ((Type.ClassType) type).getTypeArguments().get(0);
            Property propertyEl = constructProp(null, elemType);
            property = member != null
                    ? new SetProperty(member, propertyEl)
                    : new SetProperty(type, propertyEl);
        } else if (isList(type)) {
            TypeMirror elemType = ((Type.ClassType) type).getTypeArguments().get(0);
            Property propertyEl = constructProp(null, elemType);
            property = member != null
                    ? new ListProperty(member, propertyEl)
                    : new ListProperty(type, propertyEl);
        } else if (iterable(type)) {
            TypeMirror elemType = iterable(type) ? ((Type.ClassType) type).getTypeArguments().get(0) : ((ArrayType) type).getComponentType();
            Property propertyEl = constructProp(null, elemType);
            property = member != null
                    ? new ContainerProperty(member, propertyEl)
                    : new ContainerProperty(type, propertyEl);
        } else {
            property = member != null ? new Property(member) : new Property(type);
        }
        return property;
    }

}
