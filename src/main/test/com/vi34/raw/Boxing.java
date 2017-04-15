package com.vi34.raw;

import com.vi34.annotations.GenerateClasses;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by vi34 on 03/04/2017.
 */
@GenerateClasses
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Boxing {
    public int i;
    public double d;
    public char c;
    public long l;
    public float f;
    public short s;
    public byte by;
    public boolean bool;
    public Integer boxI;
    public Double boxD;
    public Character boxC;
    public Long boxL;
    public Float boxF;
    public Short boxS;
    public Byte boxBy;
    public Boolean boxBool;

}
