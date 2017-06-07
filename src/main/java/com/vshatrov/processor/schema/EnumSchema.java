package com.vshatrov.processor.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Viktor Shatrov.
 */
public class EnumSchema extends JsonSchema {

    @JsonProperty("enum")
    String[] en;

    public void setEn(String[] en) {
        this.en = en;
    }
}
