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
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class PrimitiveArray {
    public int[] ints;
}
