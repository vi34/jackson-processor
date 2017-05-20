package com.vshatrov.model;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vshatrov on 13/08/16.
 */
@JsonSerialize(using = JsonSerializer.None.class)
@JsonDeserialize(using = JsonDeserializer.None.class)
public class Complex {
    public String name;
    public int num;
    public List<Pojo> pojos;

    public Complex(String name, int num, List<Pojo> pojos) {
        this.name = name;
        this.num = num;
        this.pojos = pojos;
    }

    public Complex() {
    }

    public static Complex makeComplex(int lsize) {
        List<Pojo> list = new ArrayList<>();
        for (int i = 0; i < lsize; ++i) {
            list.add(Pojo.makePojo());
        }
        return new Complex("complex", lsize, list);
    }

    public String getName() {
        return this.name;
    }

    public int getNum() {
        return this.num;
    }

    public List<Pojo> getPojos() {
        return this.pojos;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setPojos(List<Pojo> pojos) {
        this.pojos = pojos;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Complex)) return false;
        final Complex other = (Complex) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        if (this.getNum() != other.getNum()) return false;
        final Object this$pojos = this.getPojos();
        final Object other$pojos = other.getPojos();
        if (this$pojos == null ? other$pojos != null : !this$pojos.equals(other$pojos)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        result = result * PRIME + this.getNum();
        final Object $pojos = this.getPojos();
        result = result * PRIME + ($pojos == null ? 43 : $pojos.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Complex;
    }

    public String toString() {
        return "com.vshatrov.entities.Complex(name=" + this.getName() + ", num=" + this.getNum() + ", pojos=" + this.getPojos() + ")";
    }
}
