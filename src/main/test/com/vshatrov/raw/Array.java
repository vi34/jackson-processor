package com.vshatrov.raw;

import com.vshatrov.annotations.GenerateClasses;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author Viktor Shatrov.
 */
@GenerateClasses
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Array {
    public int[] ints;
    public Pojo[] pojos;
    public List<Integer> lInts;
    public List<Pojo> lPojos;
    public Enums.En[] ens;
}
