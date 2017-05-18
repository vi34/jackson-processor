package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class Enums {
    public En enNum;

    public enum En {ONE, TWO, THREE}

    @java.beans.ConstructorProperties({"enNum"})
    public Enums(En enNum) {
        this.enNum = enNum;
    }

    public Enums() {
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Enums)) return false;
        final Enums other = (Enums) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$enNum = this.enNum;
        final Object other$enNum = other.enNum;
        if (this$enNum == null ? other$enNum != null : !this$enNum.equals(other$enNum)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $enNum = this.enNum;
        result = result * PRIME + ($enNum == null ? 43 : $enNum.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Enums;
    }

    public String toString() {
        return "com.vshatrov.raw.Enums(enNum=" + this.enNum + ")";
    }


}
