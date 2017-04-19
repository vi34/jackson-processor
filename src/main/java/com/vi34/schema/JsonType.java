package com.vi34.schema;

/**
 * Created by vi34 on 19/04/2017.
 */
public enum JsonType {
    OBJECT("object"),
    ARRAY("array"),
    STRING("string"),
    NUMBER("number"),
    INTEGER("integer"),
    BOOLEAN("boolean"),
    NULL("null"),
    ANY("any"),

    UNKNOWN("unknown"),
    UNION("union");

    private String type;

    private JsonType(String typeStr) {
        type = typeStr;
    }

    public String toString() {
        return type;
    }
}
