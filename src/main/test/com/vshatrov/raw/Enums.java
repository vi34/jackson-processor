package com.vshatrov.raw;

import com.vshatrov.annotations.GenerateClasses;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Viktor Shatrov.
 */
@GenerateClasses
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Enums {
    public En enNum;

    public enum En {ONE, TWO, THREE}
}
