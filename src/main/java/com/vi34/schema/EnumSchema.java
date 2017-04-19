package com.vi34.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vi34 on 19/04/2017.
 */
public class EnumSchema extends JsonSchema {

    @JsonProperty("enum")
    String[] en;
}
