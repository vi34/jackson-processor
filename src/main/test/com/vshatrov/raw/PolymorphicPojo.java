package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Random;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class PolymorphicPojo extends Pojo {
    private int i3;


    public PolymorphicPojo(int i1, double a2, int i3) {
        super(i1, a2);
        this.i3 = i3;
    }

    public PolymorphicPojo() {
    }

    public static PolymorphicPojo make() {
        return new PolymorphicPojo(random.nextInt(), random.nextDouble(), random.nextInt());
    }

    public int getI3() {
        return i3;
    }

    public void setI3(int i3) {
        this.i3 = i3;
    }

    @Override
    public String toString() {
        return "PolyPojo{" +
                "i1=" + i1 +
                ", i3=" + i3 +
                ", a2=" + a2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PolymorphicPojo polyPojo = (PolymorphicPojo) o;

        return i3 == polyPojo.i3;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + i3;
        return result;
    }
}
