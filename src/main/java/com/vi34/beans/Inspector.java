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
import java.util.List;

import static javax.lang.model.type.TypeKind.*;

/**
 * Created by vi34 on 18/02/2017.
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

    // TODO refactor
    private void processField(BeanDescription definition, VariableElement member) {
        Symbol.MethodSymbol getter = findGetter(member);
        Property property;

        if (!member.getModifiers().contains(Modifier.PRIVATE) || getter != null) {
            property = fabric.construct(member);
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

}
