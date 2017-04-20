package com.vi34.schema;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by vi34 on 19/04/2017.
 */
public class ArraySchema extends JsonSchema {

    @Getter
    @Setter
    JsonSchema items;
}
