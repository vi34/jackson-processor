package com.vi34.raw;

import com.vi34.annotations.Json;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * Created by vi34 on 03/04/2017.
 */
@Json
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Array {
    public int[] ints;
    public Pojo[] pojos;
    public List<Integer> lInts;
    public List<Pojo> lPojos;
}
