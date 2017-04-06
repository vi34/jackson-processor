package com.vi34.raw;

import com.vi34.annotations.Json;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by vi34 on 03/04/2017.
 */
@Json
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Pojo {
    public int i1;
    public double a2;
}