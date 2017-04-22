package com.vshatrov.schema;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Viktor Shatrov.
 */
public class ArraySchema extends JsonSchema {

    @Getter
    @Setter
    JsonSchema items;
}
