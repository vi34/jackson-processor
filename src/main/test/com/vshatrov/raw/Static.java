package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Random;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
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
