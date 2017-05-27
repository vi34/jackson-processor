package com.vshatrov.generation;

import com.squareup.javapoet.JavaFile;
import com.vshatrov.beans.BeanDescription;

/**
 * @author Viktor Shatrov.
 */
public class GenerationInfo {
    protected String typeName;
    protected JavaFile javaFile;
    protected BeanDescription unit;

    public GenerationInfo(BeanDescription unit) {
        this.unit = unit;
        this.typeName = unit.getTypeName();
    }

    public String getTypeName() {
        return this.typeName;
    }

    public BeanDescription getUnit() {
        return this.unit;
    }

    public JavaFile getJavaFile() {
        return this.javaFile;
    }

    public void setJavaFile(JavaFile javaFile) {
        this.javaFile = javaFile;
    }
}
