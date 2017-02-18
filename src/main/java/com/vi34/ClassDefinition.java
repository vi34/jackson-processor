package com.vi34;

import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vi34 on 22/12/2016.
 */
@Getter
@Setter
public class ClassDefinition {

    String simpleName;
    String packageName;
    TypeElement element;
    List<VariableElement> fields;
    //save getters for fields


    ClassDefinition(TypeElement element) {
        this.element = element;
        simpleName = element.getSimpleName().toString();
        String qualified = element.getQualifiedName().toString();
        int dot = qualified.lastIndexOf(".");
        packageName = dot > 0 ? qualified.substring(dot + 1) : "";
        fields = new ArrayList<>();
    }


}
