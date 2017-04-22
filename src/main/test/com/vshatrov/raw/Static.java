package com.vshatrov.raw;

import com.vshatrov.annotations.GenerateClasses;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Random;

/**
 * @author Viktor Shatrov.
 */
@GenerateClasses
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Static {
    public static Random random = new Random();
    public static final String s = "Skip";
    private static final String s2 = "skip";
    public int i1;
    public double a2;
}
