package com.vshatrov.raw;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Viktor Shatrov.
 */
@JsonDeserialize
@JsonSerialize(using = JsonSerializer.None.class)
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

    public Accessors(int i, double d, char c, long l, float f, short s, byte by, boolean bool, Integer boxI, Double boxD, Character boxC, Long boxL, Float boxF, Short boxS, Byte boxBy, Boolean boxBool) {
        this.i = i;
        this.d = d;
        this.c = c;
        this.l = l;
        this.f = f;
        this.s = s;
        this.by = by;
        this.bool = bool;
        this.boxI = boxI;
        this.boxD = boxD;
        this.boxC = boxC;
        this.boxL = boxL;
        this.boxF = boxF;
        this.boxS = boxS;
        this.boxBy = boxBy;
        this.boxBool = boxBool;
    }

    public Accessors() {
    }

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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Accessors)) return false;
        final Accessors other = (Accessors) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getI() != other.getI()) return false;
        if (Double.compare(this.getD(), other.getD()) != 0) return false;
        if (this.getC() != other.getC()) return false;
        if (this.getL() != other.getL()) return false;
        if (Float.compare(this.getF(), other.getF()) != 0) return false;
        if (this.getS() != other.getS()) return false;
        if (this.getBy() != other.getBy()) return false;
        if (this.isBool() != other.isBool()) return false;
        final Object this$boxI = this.getBoxI();
        final Object other$boxI = other.getBoxI();
        if (this$boxI == null ? other$boxI != null : !this$boxI.equals(other$boxI)) return false;
        final Object this$boxD = this.getBoxD();
        final Object other$boxD = other.getBoxD();
        if (this$boxD == null ? other$boxD != null : !this$boxD.equals(other$boxD)) return false;
        final Object this$boxC = this.getBoxC();
        final Object other$boxC = other.getBoxC();
        if (this$boxC == null ? other$boxC != null : !this$boxC.equals(other$boxC)) return false;
        final Object this$boxL = this.getBoxL();
        final Object other$boxL = other.getBoxL();
        if (this$boxL == null ? other$boxL != null : !this$boxL.equals(other$boxL)) return false;
        final Object this$boxF = this.getBoxF();
        final Object other$boxF = other.getBoxF();
        if (this$boxF == null ? other$boxF != null : !this$boxF.equals(other$boxF)) return false;
        final Object this$boxS = this.getBoxS();
        final Object other$boxS = other.getBoxS();
        if (this$boxS == null ? other$boxS != null : !this$boxS.equals(other$boxS)) return false;
        final Object this$boxBy = this.getBoxBy();
        final Object other$boxBy = other.getBoxBy();
        if (this$boxBy == null ? other$boxBy != null : !this$boxBy.equals(other$boxBy)) return false;
        final Object this$boxBool = this.getBoxBool();
        final Object other$boxBool = other.getBoxBool();
        if (this$boxBool == null ? other$boxBool != null : !this$boxBool.equals(other$boxBool)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getI();
        final long $d = Double.doubleToLongBits(this.getD());
        result = result * PRIME + (int) ($d >>> 32 ^ $d);
        result = result * PRIME + this.getC();
        final long $l = this.getL();
        result = result * PRIME + (int) ($l >>> 32 ^ $l);
        result = result * PRIME + Float.floatToIntBits(this.getF());
        result = result * PRIME + this.getS();
        result = result * PRIME + this.getBy();
        result = result * PRIME + (this.isBool() ? 79 : 97);
        final Object $boxI = this.getBoxI();
        result = result * PRIME + ($boxI == null ? 43 : $boxI.hashCode());
        final Object $boxD = this.getBoxD();
        result = result * PRIME + ($boxD == null ? 43 : $boxD.hashCode());
        final Object $boxC = this.getBoxC();
        result = result * PRIME + ($boxC == null ? 43 : $boxC.hashCode());
        final Object $boxL = this.getBoxL();
        result = result * PRIME + ($boxL == null ? 43 : $boxL.hashCode());
        final Object $boxF = this.getBoxF();
        result = result * PRIME + ($boxF == null ? 43 : $boxF.hashCode());
        final Object $boxS = this.getBoxS();
        result = result * PRIME + ($boxS == null ? 43 : $boxS.hashCode());
        final Object $boxBy = this.getBoxBy();
        result = result * PRIME + ($boxBy == null ? 43 : $boxBy.hashCode());
        final Object $boxBool = this.getBoxBool();
        result = result * PRIME + ($boxBool == null ? 43 : $boxBool.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Accessors;
    }

    public String toString() {
        return "com.vshatrov.raw.Accessors(i=" + this.getI() + ", d=" + this.getD() + ", c=" + this.getC() + ", l=" + this.getL() + ", f=" + this.getF() + ", s=" + this.getS() + ", by=" + this.getBy() + ", bool=" + this.isBool() + ", boxI=" + this.getBoxI() + ", boxD=" + this.getBoxD() + ", boxC=" + this.getBoxC() + ", boxL=" + this.getBoxL() + ", boxF=" + this.getBoxF() + ", boxS=" + this.getBoxS() + ", boxBy=" + this.getBoxBy() + ", boxBool=" + this.getBoxBool() + ")";
    }
}
