package com.vshatrov.beans;


import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.util.Pair;
import com.vshatrov.beans.properties.Property;
import com.vshatrov.beans.properties.PropertyFabric;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static javax.lang.model.type.TypeKind.*;

/**
 * @author Viktor Shatrov.
 */
public class Inspector {
    private Elements elementUtils;
    private List<VariableElement> fields;
    private List<Symbol.MethodSymbol> methods;
    private PropertyFabric fabric;

    public Inspector(Elements elementUtils) {
        this.elementUtils = elementUtils;
        fields = new ArrayList<>();
        methods = new ArrayList<>();
        fabric = new PropertyFabric();
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
        Symbol.MethodSymbol setter = findSetter(member);
        Property property;

        if (!member.getModifiers().contains(Modifier.PRIVATE) || getter != null && setter != null) {
            property = fabric.construct(member);
            definition.getProps().add(property);
            if (getter != null && setter != null) {
                property.setGetter(getter.getSimpleName().toString());
                property.setSetter(setter.getSimpleName().toString());
            }
            getJsonProperty(member)
                    .flatMap(ann -> extractAnnotationValue(ann, "value()"))
                    .ifPresent(n -> property.setName((String)n));
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
                    || getJsonProperty(method).isPresent()
                        && compareJsonPropertyName(getJsonProperty(method).get(), field)
                    ) {
                getter = method;
                break;
            }
        }
        return getter;
    }

    private Symbol.MethodSymbol findSetter(VariableElement field) {
        Symbol.MethodSymbol setter = null;
        String name = field.getSimpleName().toString().toLowerCase();
        for (Symbol.MethodSymbol method : methods) {
            String mName = method.getSimpleName().toString().toLowerCase();
            if (mName.startsWith("set") && mName.length() > 3 && name.equals(mName.substring(3))
                    || getJsonProperty(method).isPresent()
                        && compareJsonPropertyName(getJsonProperty(method).get(), field)
                        && method.getReturnType().getKind().equals(VOID)
                        && method.getParameters().size() == 1
                        && method.getParameters().get(0).asType().equals(field.asType())
                    ) {
                setter = method;
                break;
            }
        }
        return setter;
    }

    private Optional<Object> extractAnnotationValue(AnnotationMirror annotation, String key) {
        return annotation.getElementValues().entrySet().stream().filter(p -> p.getKey().toString().equals(key))
                .findAny().map(p -> p.getValue().getValue());
    }

    private boolean compareJsonPropertyName(AnnotationMirror jsonProp, VariableElement field) {
      return extractAnnotationValue(jsonProp, "value()")
              .map(p -> p.toString().equals(field.getSimpleName().toString()))
              .orElse(false);
    }


    private Optional<? extends AnnotationMirror> getJsonProperty(Element symbol) {
        return symbol.getAnnotationMirrors().stream()
                .filter(ann -> ann.getAnnotationType().toString().equals("com.fasterxml.jackson.annotation.JsonProperty"))
                .findAny();
    }

}