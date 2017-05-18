package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class PrimitiveArray {
    public int[] ints;


    @java.beans.ConstructorProperties({"ints"})
    public PrimitiveArray(int[] ints) {
        this.ints = ints;
    }

    public PrimitiveArray() {
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PrimitiveArray)) return false;
        final PrimitiveArray other = (PrimitiveArray) o;
        if (!other.canEqual((Object) this)) return false;
        if (!java.util.Arrays.equals(this.ints, other.ints)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + java.util.Arrays.hashCode(this.ints);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PrimitiveArray;
    }

    public String toString() {
        return "com.vshatrov.raw.PrimitiveArray(ints=" + java.util.Arrays.toString(this.ints) + ")";
    }
}
