package com.vi34.raw;

import com.vi34.annotations.GenerateClasses;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by vi34 on 03/04/2017.
 */
@GenerateClasses
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Getters {
    private int i;
    private double d;
    private char c;
    private long l;
    private float f;
    private short s;
    private byte by;
    private boolean bool;
    private Integer boxI;
    private Double boxD;
    private Character boxC;
    private Long boxL;
    private Float boxF;
    private Short boxS;
    private Byte boxBy;
    private Boolean boxBool;

    public int getI() {
        return this.i;
    }

    public double getD() {
        return this.d;
    }

    public char getC() {
        return this.c;
    }

    public long getL() {
        return this.l;
    }

    public float getF() {
        return this.f;
    }

    public short getS() {
        return this.s;
    }

    public byte getBy() {
        return this.by;
    }

    public boolean isBool() {
        return this.bool;
    }

    public Integer getBoxI() {
        return this.boxI;
    }

    public Double getBoxD() {
        return this.boxD;
    }

    public Character getBoxC() {
        return this.boxC;
    }

    public Long getBoxL() {
        return this.boxL;
    }

    public Float getBoxF() {
        return this.boxF;
    }

    public Short getBoxS() {
        return this.boxS;
    }

    public Byte getBoxBy() {
        return this.boxBy;
    }

    public Boolean getBoxBool() {
        return this.boxBool;
    }
}
