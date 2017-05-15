package com.vshatrov.beans;

import com.squareup.javapoet.ClassName;
import com.vshatrov.beans.properties.Property;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Viktor Shatrov.
 */
@Getter
@Setter
public class BeanDescription {

    String packageName;
    String typeName;
    TypeMirror type;
    TypeElement element;
    List<Property> props;
    AnnotationMirror jsonSerialize;
    AnnotationMirror jsonDeserialize;
    ClassName className;

    public BeanDescription(TypeElement element) {
        this.element = element;
        String qualified = element.getQualifiedName().toString();
        int dot = qualified.lastIndexOf(".");
        packageName = dot > 0 ? qualified.substring(0, dot) : "";
        props = new ArrayList<>();
        type = element.asType();
        typeName = type.toString();
        className = ClassName.get(element);
    }

    public String getSimpleName() {
        return className.simpleName();
    }


}
