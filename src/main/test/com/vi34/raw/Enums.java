package com.vi34.raw;

import com.vi34.annotations.GenerateClasses;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by vi34 on 03/04/2017.
 */
@GenerateClasses
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Enums {
    public En enNum;

    public enum En {ONE, TWO, THREE}
}
