package com.vshatrov.raw;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Viktor Shatrov.
 */
@JsonDeserialize
@JsonSerialize(using = JsonSerializer.None.class)
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Accessors {
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

    public void setI(int i) {
        this.i = i;
    }

    public void setD(double d) {
        this.d = d;
    }

    public void setC(char c) {
        this.c = c;
    }

    public void setL(long l) {
        this.l = l;
    }

    public void setF(float f) {
        this.f = f;
    }

    public void setS(short s) {
        this.s = s;
    }

    public void setBy(byte by) {
        this.by = by;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public void setBoxI(Integer boxI) {
        this.boxI = boxI;
    }

    public void setBoxD(Double boxD) {
        this.boxD = boxD;
    }

    public void setBoxC(Character boxC) {
        this.boxC = boxC;
    }

    public void setBoxL(Long boxL) {
        this.boxL = boxL;
    }

    public void setBoxF(Float boxF) {
        this.boxF = boxF;
    }

    public void setBoxS(Short boxS) {
        this.boxS = boxS;
    }

    public void setBoxBy(Byte boxBy) {
        this.boxBy = boxBy;
    }

    public void setBoxBool(Boolean boxBool) {
        this.boxBool = boxBool;
    }
}
