package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class Complex {
    public int i1;
    public Pojo pojo;

    public Complex(int i1, Pojo pojo) {
        this.i1 = i1;
        this.pojo = pojo;
    }

    public Complex() {
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Complex)) return false;
        final Complex other = (Complex) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.i1 != other.i1) return false;
        final Object this$pojo = this.pojo;
        final Object other$pojo = other.pojo;
        if (this$pojo == null ? other$pojo != null : !this$pojo.equals(other$pojo)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.i1;
        final Object $pojo = this.pojo;
        result = result * PRIME + ($pojo == null ? 43 : $pojo.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Complex;
    }

    public String toString() {
        return "com.vshatrov.raw.Complex(i1=" + this.i1 + ", pojo=" + this.pojo + ")";
    }
}
