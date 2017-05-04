package com.vshatrov.beans;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.vshatrov.annotations.AlternativeNames;
import com.vshatrov.beans.properties.Property;
import com.vshatrov.beans.properties.PropertyFabric;
import com.vshatrov.utils.Utils;

import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.vshatrov.utils.Utils.getAnnotation;
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

        if (takeField(member, getter, setter)) {
            property = fabric.construct(member);
            definition.getProps().add(property);
            if (getter != null && setter != null) {
                property.setGetter(getter.getSimpleName().toString());
                property.setSetter(setter.getSimpleName().toString());
            }
            getAnnotation(member, JsonProperty.class)
                    .flatMap(ann -> Utils.extractAnnotationValue(ann, "value()"))
                    .ifPresent(n -> property.setName((String)n));
            getAnnotation(member, AlternativeNames.class)
                    .flatMap(ann -> Utils.extractAnnotationValue(ann, "value()"))
                    .ifPresent(arr -> property.setAlternativeNames(((List<Attribute.Constant>)arr)
                            .stream()
                            .map(c -> c.getValue().toString())
                            .collect(Collectors.toList()))
                    );
        }
    }

    private boolean takeField(VariableElement member,  Symbol.MethodSymbol getter, Symbol.MethodSymbol setter) {
        if (member.getModifiers().contains(Modifier.TRANSIENT)
                || member.getModifiers().contains(Modifier.STATIC)) return false;
        return !member.getModifiers().contains(Modifier.PRIVATE) || getter != null && setter != null;
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
                    || getAnnotation(method, JsonProperty.class).isPresent()
                        && compareJsonPropertyName(getAnnotation(method, JsonProperty.class).get(), field)
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
                    || getAnnotation(method, JsonProperty.class).isPresent()
                        && compareJsonPropertyName(getAnnotation(method, JsonProperty.class).get(), field)
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

    private boolean compareJsonPropertyName(AnnotationMirror jsonProp, VariableElement field) {
      return Utils.extractAnnotationValue(jsonProp, "value()")
              .map(p -> p.toString().equals(field.getSimpleName().toString()))
              .orElse(false);
    }

}
