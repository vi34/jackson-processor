package com.vi34;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
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


    List<ClassDefinition> inspect(TypeElement element) {
        List<ClassDefinition> result = new ArrayList<>();
        List<? extends Element> members = elementUtils.getAllMembers(element);
        ClassDefinition definition = new ClassDefinition(element);
        for (Element member: members) {
            if (member.getKind().equals(ElementKind.FIELD)) {
                definition.fields.add((VariableElement) member);
            }
        }
        result.add(definition);
        return result;
    }

}
