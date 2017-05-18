package com.vshatrov.raw.annotations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class Renaming {
    @JsonProperty("first")
    public int i1;
    @JsonProperty("second")
    public double a2;

    private String surname;

    @java.beans.ConstructorProperties({"i1", "a2", "surname"})
    public Renaming(int i1, double a2, String surname) {
        this.i1 = i1;
        this.a2 = a2;
        this.surname = surname;
    }

    public Renaming() {
    }

    @JsonProperty("surname")
    public String getName() {
        return surname;
    }

    @JsonProperty("surname")
    public void setName(String name) {
        surname = name;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Renaming)) return false;
        final Renaming other = (Renaming) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.i1 != other.i1) return false;
        if (Double.compare(this.a2, other.a2) != 0) return false;
        final Object this$surname = this.surname;
        final Object other$surname = other.surname;
        if (this$surname == null ? other$surname != null : !this$surname.equals(other$surname)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.i1;
        final long $a2 = Double.doubleToLongBits(this.a2);
        result = result * PRIME + (int) ($a2 >>> 32 ^ $a2);
        final Object $surname = this.surname;
        result = result * PRIME + ($surname == null ? 43 : $surname.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Renaming;
    }

    public String toString() {
        return "com.vshatrov.raw.annotations.Renaming(i1=" + this.i1 + ", a2=" + this.a2 + ", surname=" + this.surname + ")";
    }
}
