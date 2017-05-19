package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Random;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class Pojo {
    public int i1;
    public double a2;


    public Pojo(int i1, double a2) {
        this.i1 = i1;
        this.a2 = a2;
    }

    public Pojo() {
    }

    static Random random = new Random();
    public static Pojo make() {
        return new Pojo(random.nextInt(), random.nextDouble());
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Pojo)) return false;
        final Pojo other = (Pojo) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.i1 != other.i1) return false;
        if (Double.compare(this.a2, other.a2) != 0) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.i1;
        final long $a2 = Double.doubleToLongBits(this.a2);
        result = result * PRIME + (int) ($a2 >>> 32 ^ $a2);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Pojo;
    }

    public String toString() {
        return "com.vshatrov.raw.Pojo(i1=" + this.i1 + ", a2=" + this.a2 + ")";
    }
}
