package com.vshatrov.raw;

/**
 * @author Viktor Shatrov.
 */
public class UnknownClass {
    int i1;
    String s2;


    @java.beans.ConstructorProperties({"i1", "s2"})
    public UnknownClass(int i1, String s2) {
        this.i1 = i1;
        this.s2 = s2;
    }

    public UnknownClass() {
    }

    public int getI1() {
        return this.i1;
    }

    public String getS2() {
        return this.s2;
    }

    public void setI1(int i1) {
        this.i1 = i1;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof UnknownClass)) return false;
        final UnknownClass other = (UnknownClass) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getI1() != other.getI1()) return false;
        final Object this$s2 = this.getS2();
        final Object other$s2 = other.getS2();
        if (this$s2 == null ? other$s2 != null : !this$s2.equals(other$s2)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getI1();
        final Object $s2 = this.getS2();
        result = result * PRIME + ($s2 == null ? 43 : $s2.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UnknownClass;
    }
}
