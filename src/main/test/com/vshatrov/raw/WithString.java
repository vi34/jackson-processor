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
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class WithString {
    public int i1;
    public double a2;
    public String s1;
    public String name;
}
