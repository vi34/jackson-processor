package com.vshatrov.beans.properties;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.vshatrov.GenerationException;
import com.vshatrov.schema.JsonType;
import com.vshatrov.utils.Utils;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;

/**
 * @author Viktor Shatrov.
 */
public class MapProp extends Property {

    KeyProp key;
    Property value;

    MapProp(VariableElement element, KeyProp key, Property value) {
        super(element);
        this.key = key;
        this.value = value;
        value.dynamicAccessor = (var) -> var + ".getValue()";
        jsonType = JsonType.OBJECT;
    }

    MapProp(TypeMirror typeMirror, KeyProp key, Property value) {
        super(typeMirror);
        this.key = key;
        this.value = value;
        value.dynamicAccessor = (var) -> var + ".getValue()";
        jsonType = JsonType.OBJECT;
    }

    public KeyProp getKey() {
        return this.key;
    }

    public Property getValue() {
        return this.value;
    }

    @Override
    public CodeBlock defaultInstance() {
        if (isInterface) {
            return CodeBlock.builder()
                    .add("new $T()", ParameterizedTypeName.get(ClassName.get(HashMap.class),
                            key.getTName(), value.getTName()))
                    .add(";\n$]")
                    .build();
        } else {
            return super.defaultInstance();
        }
    }

    public static class KeyProp extends Property {

        private final boolean primitive;

        KeyProp(TypeMirror type) {
            super(type);
            primitive = type.getKind().isPrimitive();
        }

        @Override
        protected String modifyAccess(String accessor) {
            if (primitive) {
                return "\"\" + " + accessor;
            } else {
                return accessor + ".toString()";
            }
        }

        @Override
        public String getAccessor(String var) {
            return var + ".getKey()";
        }

        @Override
        public String writeMethod(String varName) throws GenerationException {
            return modifyAccess(getAccessor(varName));
        }

        //TODO: add String constructors for key-objects
        @Override
        public String parseMethod(String keyStr) throws GenerationException {
            return typeName + ".valueOf(" + keyStr + ")";
        }
    }
}
