package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
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
