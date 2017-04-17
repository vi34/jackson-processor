package com.vi34.beans;


import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import javax.lang.model.element.*;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.lang.model.type.TypeKind.*;

/**
 * Created by vi34 on 18/02/2017.
 */
public class Inspector {
    private Elements elementUtils;
    private Types typeUtils;
    private List<VariableElement> fields;
    private List<Symbol.MethodSymbol> methods;

    public Inspector(Elements elementUtils, Types typeUtils) {
        this.elementUtils = elementUtils;
        this.typeUtils = typeUtils;
        fields = new ArrayList<>();
        methods = new ArrayList<>();
    }

    public BeanDescription inspect(TypeElement element) {
        List<? extends Element> members = elementUtils.getAllMembers(element);
        BeanDescription definition = new BeanDescription(element);
        for (Element member: members) {
            if (member.getModifiers().contains(Modifier.STATIC))
                continue;
            if (member.getKind().equals(ElementKind.FIELD)) {
                VariableElement field = (VariableElement) member;
                fields.add(field);
            } else if (member.getKind().equals(ElementKind.METHOD)) {
                methods.add((Symbol.MethodSymbol) member);
            }
        }
        fields.forEach(field -> processField(definition, field));
        return definition;
    }

    private void processField(BeanDescription definition, VariableElement member) {
        Symbol.MethodSymbol getter = findGetter(member);
        Property property;

        if (!member.getModifiers().contains(Modifier.PRIVATE) || getter != null) {
            TypeMirror type = member.asType();
            if (type.getKind().equals(ARRAY) || iterable(type)) {// TODO add Maps
                type = iterable(type) ? ((Type.ClassType) type).getTypeArguments().get(0) : ((ArrayType) type).getComponentType();
                Property propertyEl = new ElemProp(type);
                fillWithType(type, propertyEl);
                property = new ContainerProp(member, propertyEl);
            } else if (isEnum(type)) {
                property = new EnumProp(member);
                property.setSimple(true);
            } else {
                property = new Property(member);
                fillWithType(type, property);
            }
            definition.getProps().add(property);
            property.setField(getter == null);
            if (getter != null) {
                property.setGetter(getter.getSimpleName().toString());
            }
        }
    }

    private Symbol.MethodSymbol findGetter(VariableElement field) {
        Symbol.MethodSymbol getter = null;
        String name = field.getSimpleName().toString().toLowerCase();
        for (Symbol.MethodSymbol method : methods) {
            //TODO handle jackson annotations. JsonGetter, JsonProperty etc.
            String mName = method.getSimpleName().toString().toLowerCase();
            if ((mName.startsWith("get") && mName.length() > 3 && name.equals(mName.substring(3)))
                    || (field.asType().getKind().equals(BOOLEAN) && mName.startsWith("is")
                        && mName.length() > 2 &&  name.equals(mName.substring(2)))
                    ) {
                getter = method;
                break;
            }
        }
        return getter;
    }

    private void fillWithType(TypeMirror type, Property property) {
        property.setNumber(isNumber(type));
        property.setSimple(computeSimple(type));
    }

    private boolean isEnum(TypeMirror type) {
        List<? extends TypeMirror> supertypes = typeUtils.directSupertypes(type);
        return supertypes.size() > 0 && supertypes.stream().anyMatch(t -> typeUtils.erasure(t).toString().equals("java.lang.Enum"));
    }

    private boolean computeSimple(TypeMirror type) {
        return type.getKind().isPrimitive() || isNumber(type)
                || type.toString().equals("java.lang.String")
                || type.toString().equals("java.lang.Character")
                || type.toString().equals("java.lang.Boolean");
    }

    private boolean iterable(TypeMirror type) {
        List<? extends TypeMirror> supertypes = typeUtils.directSupertypes(type);
        return supertypes.size() > 0 && supertypes.stream().anyMatch(t -> typeUtils.erasure(t).toString().equals("java.util.Collection"));
    }

    private boolean isNumber(TypeMirror type) {
        List<? extends TypeMirror> supertypes = typeUtils.directSupertypes(type);
        if (supertypes.size() > 0 && supertypes.get(0).toString().equals("java.lang.Number"))
            return true;

        TypeKind kind = type.getKind();
        return kind == INT || kind == LONG || kind == SHORT || kind == BYTE || kind == DOUBLE || kind == FLOAT;
    }

}
