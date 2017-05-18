package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class Array {
    public int[] ints;
    public Pojo[] pojos;
    public List<Integer> lInts;
    public List<Pojo> lPojos;
    public Enums.En[] ens;


    public Array(int[] ints, Pojo[] pojos, List<Integer> lInts, List<Pojo> lPojos, Enums.En[] ens) {
        this.ints = ints;
        this.pojos = pojos;
        this.lInts = lInts;
        this.lPojos = lPojos;
        this.ens = ens;
    }

    public Array() {
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Array)) return false;
        final Array other = (Array) o;
        if (!other.canEqual((Object) this)) return false;
        if (!java.util.Arrays.equals(this.ints, other.ints)) return false;
        if (!java.util.Arrays.deepEquals(this.pojos, other.pojos)) return false;
        final Object this$lInts = this.lInts;
        final Object other$lInts = other.lInts;
        if (this$lInts == null ? other$lInts != null : !this$lInts.equals(other$lInts)) return false;
        final Object this$lPojos = this.lPojos;
        final Object other$lPojos = other.lPojos;
        if (this$lPojos == null ? other$lPojos != null : !this$lPojos.equals(other$lPojos)) return false;
        if (!java.util.Arrays.deepEquals(this.ens, other.ens)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + java.util.Arrays.hashCode(this.ints);
        result = result * PRIME + java.util.Arrays.deepHashCode(this.pojos);
        final Object $lInts = this.lInts;
        result = result * PRIME + ($lInts == null ? 43 : $lInts.hashCode());
        final Object $lPojos = this.lPojos;
        result = result * PRIME + ($lPojos == null ? 43 : $lPojos.hashCode());
        result = result * PRIME + java.util.Arrays.deepHashCode(this.ens);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Array;
    }

    public String toString() {
        return "com.vshatrov.raw.Array(ints=" + java.util.Arrays.toString(this.ints) + ", pojos=" + java.util.Arrays.deepToString(this.pojos) + ", lInts=" + this.lInts + ", lPojos=" + this.lPojos + ", ens=" + java.util.Arrays.deepToString(this.ens) + ")";
    }
}
