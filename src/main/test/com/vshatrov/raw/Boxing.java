package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
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

    public Boxing(int i, double d, char c, long l, float f, short s, byte by, boolean bool, Integer boxI, Double boxD, Character boxC, Long boxL, Float boxF, Short boxS, Byte boxBy, Boolean boxBool) {
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

    public Boxing() {
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Boxing)) return false;
        final Boxing other = (Boxing) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.i != other.i) return false;
        if (Double.compare(this.d, other.d) != 0) return false;
        if (this.c != other.c) return false;
        if (this.l != other.l) return false;
        if (Float.compare(this.f, other.f) != 0) return false;
        if (this.s != other.s) return false;
        if (this.by != other.by) return false;
        if (this.bool != other.bool) return false;
        final Object this$boxI = this.boxI;
        final Object other$boxI = other.boxI;
        if (this$boxI == null ? other$boxI != null : !this$boxI.equals(other$boxI)) return false;
        final Object this$boxD = this.boxD;
        final Object other$boxD = other.boxD;
        if (this$boxD == null ? other$boxD != null : !this$boxD.equals(other$boxD)) return false;
        final Object this$boxC = this.boxC;
        final Object other$boxC = other.boxC;
        if (this$boxC == null ? other$boxC != null : !this$boxC.equals(other$boxC)) return false;
        final Object this$boxL = this.boxL;
        final Object other$boxL = other.boxL;
        if (this$boxL == null ? other$boxL != null : !this$boxL.equals(other$boxL)) return false;
        final Object this$boxF = this.boxF;
        final Object other$boxF = other.boxF;
        if (this$boxF == null ? other$boxF != null : !this$boxF.equals(other$boxF)) return false;
        final Object this$boxS = this.boxS;
        final Object other$boxS = other.boxS;
        if (this$boxS == null ? other$boxS != null : !this$boxS.equals(other$boxS)) return false;
        final Object this$boxBy = this.boxBy;
        final Object other$boxBy = other.boxBy;
        if (this$boxBy == null ? other$boxBy != null : !this$boxBy.equals(other$boxBy)) return false;
        final Object this$boxBool = this.boxBool;
        final Object other$boxBool = other.boxBool;
        if (this$boxBool == null ? other$boxBool != null : !this$boxBool.equals(other$boxBool)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.i;
        final long $d = Double.doubleToLongBits(this.d);
        result = result * PRIME + (int) ($d >>> 32 ^ $d);
        result = result * PRIME + this.c;
        final long $l = this.l;
        result = result * PRIME + (int) ($l >>> 32 ^ $l);
        result = result * PRIME + Float.floatToIntBits(this.f);
        result = result * PRIME + this.s;
        result = result * PRIME + this.by;
        result = result * PRIME + (this.bool ? 79 : 97);
        final Object $boxI = this.boxI;
        result = result * PRIME + ($boxI == null ? 43 : $boxI.hashCode());
        final Object $boxD = this.boxD;
        result = result * PRIME + ($boxD == null ? 43 : $boxD.hashCode());
        final Object $boxC = this.boxC;
        result = result * PRIME + ($boxC == null ? 43 : $boxC.hashCode());
        final Object $boxL = this.boxL;
        result = result * PRIME + ($boxL == null ? 43 : $boxL.hashCode());
        final Object $boxF = this.boxF;
        result = result * PRIME + ($boxF == null ? 43 : $boxF.hashCode());
        final Object $boxS = this.boxS;
        result = result * PRIME + ($boxS == null ? 43 : $boxS.hashCode());
        final Object $boxBy = this.boxBy;
        result = result * PRIME + ($boxBy == null ? 43 : $boxBy.hashCode());
        final Object $boxBool = this.boxBool;
        result = result * PRIME + ($boxBool == null ? 43 : $boxBool.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Boxing;
    }

    public String toString() {
        return "com.vshatrov.raw.Boxing(i=" + this.i + ", d=" + this.d + ", c=" + this.c + ", l=" + this.l + ", f=" + this.f + ", s=" + this.s + ", by=" + this.by + ", bool=" + this.bool + ", boxI=" + this.boxI + ", boxD=" + this.boxD + ", boxC=" + this.boxC + ", boxL=" + this.boxL + ", boxF=" + this.boxF + ", boxS=" + this.boxS + ", boxBy=" + this.boxBy + ", boxBool=" + this.boxBool + ")";
    }
}
