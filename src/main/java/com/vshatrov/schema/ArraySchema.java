package com.vshatrov.schema;

/**
 * @author Viktor Shatrov.
 */
public class ArraySchema extends JsonSchema {

    JsonSchema items;

    public JsonSchema getItems() {
        return this.items;
    }

    public void setItems(JsonSchema items) {
        this.items = items;
    }
}
