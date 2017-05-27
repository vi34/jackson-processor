package com.vshatrov.raw.annotations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Viktor Shatrov.
 */

@JsonSerialize
@JsonPropertyOrder({"i1", "i2", "i3", "i4"})
public class Order {
    public int i3;
    public int i4;
    @JsonIgnore
    private int ignored;
    private int i2;
    private int i1;

    public Order() {
    }


    public int getI2() {
        return i2;
    }

    public void setI2(int i2) {
        this.i2 = i2;
    }

    public int getIgnored() {
        return ignored;
    }

    public void setIgnored(int ignored) {
        this.ignored = ignored;
    }

    public int getI1() {
        return i1;
    }

    public void setI1(int i1) {
        this.i1 = i1;
    }
}
