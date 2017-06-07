package com.vshatrov.processor.schema;

/**
 * @author Viktor Shatrov.
 */
public enum JsonType {
    OBJECT("object"),
    ARRAY("array"),
    STRING("string"),
    NUMBER("number"),
    INTEGER("integer"),
    BOOLEAN("boolean"),
    NULL("null");

    private String type;

    private JsonType(String typeStr) {
        type = typeStr;
    }

    public String toString() {
        return type;
    }
}
