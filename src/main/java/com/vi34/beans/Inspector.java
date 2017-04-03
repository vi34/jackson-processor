package com.vi34.beans;

import com.vi34.beans.ClassDefinition;

import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vi34 on 18/02/2017.
 */
public class Inspector {
    private Elements elementUtils;

    public Inspector(Elements elementUtils) {
        this.elementUtils = elementUtils;
    }

    //Map<String, >

    public List<ClassDefinition> inspect(TypeElement element) {
        List<ClassDefinition> result = new ArrayList<>();
        List<? extends Element> members = elementUtils.getAllMembers(element);
        ClassDefinition definition = new ClassDefinition(element);
        for (Element member: members) {
            if (member.getKind().equals(ElementKind.FIELD)) {
                if (!member.getModifiers().contains(Modifier.PRIVATE)) {
                    Property property = new Property((VariableElement) member);
                    definition.getProps().add(property);
                }
            }
        }
        result.add(definition);
        return result;
    }

}
