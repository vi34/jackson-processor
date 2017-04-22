package com.vshatrov.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

/**
 * @author Viktor Shatrov.
 */
public class EnumSchema extends JsonSchema {

    @JsonProperty("enum")
    @Setter
    String[] en;
}
