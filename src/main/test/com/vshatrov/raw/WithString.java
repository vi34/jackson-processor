package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class WithString {
    public int i1;
    public double a2;
    public String s1;
    public String name;


    @java.beans.ConstructorProperties({"i1", "a2", "s1", "name"})
    public WithString(int i1, double a2, String s1, String name) {
        this.i1 = i1;
        this.a2 = a2;
        this.s1 = s1;
        this.name = name;
    }

    public WithString() {
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof WithString)) return false;
        final WithString other = (WithString) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.i1 != other.i1) return false;
        if (Double.compare(this.a2, other.a2) != 0) return false;
        final Object this$s1 = this.s1;
        final Object other$s1 = other.s1;
        if (this$s1 == null ? other$s1 != null : !this$s1.equals(other$s1)) return false;
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.i1;
        final long $a2 = Double.doubleToLongBits(this.a2);
        result = result * PRIME + (int) ($a2 >>> 32 ^ $a2);
        final Object $s1 = this.s1;
        result = result * PRIME + ($s1 == null ? 43 : $s1.hashCode());
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof WithString;
    }

    public String toString() {
        return "com.vshatrov.raw.WithString(i1=" + this.i1 + ", a2=" + this.a2 + ", s1=" + this.s1 + ", name=" + this.name + ")";
    }
}
