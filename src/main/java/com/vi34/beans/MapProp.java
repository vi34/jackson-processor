package com.vi34.beans;

import com.vi34.schema.JsonType;
import lombok.Getter;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by vi34 on 06/04/2017.
 */
@Getter
public class MapProp extends Property {

    KeyProp key;
    Property value;

    MapProp(VariableElement element, KeyProp key, Property value) {
        super(element);
        this.key = key;
        this.value = value;
        value.accessor = (var) -> var + ".getValue()";
        jsonType = JsonType.OBJECT;
    }

    MapProp(TypeMirror typeMirror, KeyProp key, Property value) {
        super(typeMirror);
        this.key = key;
        this.value = value;
        value.accessor = (var) -> var + ".getValue()";
        jsonType = JsonType.OBJECT;
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
            return modifyAccess(var + ".getKey()");
        }
    }
}
