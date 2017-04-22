package com.vshatrov.raw;

import com.vshatrov.annotations.GenerateClasses;
import lombok.*;

/**
 * @author Viktor Shatrov.
 */
@GenerateClasses
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
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
