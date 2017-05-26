package com.vshatrov.beans;

import com.squareup.javapoet.ClassName;
import com.vshatrov.beans.properties.Property;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * Collected information about class for which implementations being generated.
 * @author Viktor Shatrov.
 */
public class BeanDescription {

    private String packageName;
    private String typeName;
    private TypeMirror type;
    private TypeElement element;
    private List<Property> props;
    private AnnotationMirror jsonSerialize;
    private AnnotationMirror jsonDeserialize;
    private ClassName className;

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


    public String getPackageName() {
        return this.packageName;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public TypeMirror getType() {
        return this.type;
    }

    public TypeElement getElement() {
        return this.element;
    }

    public List<Property> getProps() {
        return this.props;
    }

    public AnnotationMirror getJsonSerialize() {
        return this.jsonSerialize;
    }

    public AnnotationMirror getJsonDeserialize() {
        return this.jsonDeserialize;
    }

    public ClassName getClassName() {
        return this.className;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setType(TypeMirror type) {
        this.type = type;
    }

    public void setElement(TypeElement element) {
        this.element = element;
    }

    public void setProps(List<Property> props) {
        this.props = props;
    }

    public void setJsonSerialize(AnnotationMirror jsonSerialize) {
        this.jsonSerialize = jsonSerialize;
    }

    public void setJsonDeserialize(AnnotationMirror jsonDeserialize) {
        this.jsonDeserialize = jsonDeserialize;
    }

    public void setClassName(ClassName className) {
        this.className = className;
    }
}
