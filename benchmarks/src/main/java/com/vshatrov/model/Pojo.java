package com.vshatrov.model;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author vshatrov on 13/08/16.
 */
@JsonSerialize(using = JsonSerializer.None.class)
@JsonDeserialize(using = JsonDeserializer.None.class)
public class Pojo {
    int i1;
    String str;
    List<Integer> list;
    boolean bool;
    Double aDouble;
    private int prInt;
    char aChar;

    static Random random = new Random();

    public Pojo(int i1, String str, List<Integer> list, boolean bool, Double aDouble, int prInt, char aChar) {
        this.i1 = i1;
        this.str = str;
        this.list = list;
        this.bool = bool;
        this.aDouble = aDouble;
        this.prInt = prInt;
        this.aChar = aChar;
    }

    public Pojo() {
    }

    public static Pojo makePojo() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            list.add(random.nextInt());
        }
        return new Pojo(random.nextInt(), "pojo", list,
                random.nextBoolean(), random.nextDouble(), random.nextInt(), 'a');
    }

    public int getI1() {
        return this.i1;
    }

    public String getStr() {
        return this.str;
    }

    public List<Integer> getList() {
        return this.list;
    }

    public boolean isBool() {
        return this.bool;
    }

    public Double getADouble() {
        return this.aDouble;
    }

    public int getPrInt() {
        return this.prInt;
    }

    public char getAChar() {
        return this.aChar;
    }

    public void setI1(int i1) {
        this.i1 = i1;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public void setADouble(Double aDouble) {
        this.aDouble = aDouble;
    }

    public void setPrInt(int prInt) {
        this.prInt = prInt;
    }

    public void setAChar(char aChar) {
        this.aChar = aChar;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Pojo)) return false;
        final Pojo other = (Pojo) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getI1() != other.getI1()) return false;
        final Object this$str = this.getStr();
        final Object other$str = other.getStr();
        if (this$str == null ? other$str != null : !this$str.equals(other$str)) return false;
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null ? other$list != null : !this$list.equals(other$list)) return false;
        if (this.isBool() != other.isBool()) return false;
        final Object this$aDouble = this.getADouble();
        final Object other$aDouble = other.getADouble();
        if (this$aDouble == null ? other$aDouble != null : !this$aDouble.equals(other$aDouble)) return false;
        if (this.getPrInt() != other.getPrInt()) return false;
        if (this.getAChar() != other.getAChar()) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getI1();
        final Object $str = this.getStr();
        result = result * PRIME + ($str == null ? 43 : $str.hashCode());
        final Object $list = this.getList();
        result = result * PRIME + ($list == null ? 43 : $list.hashCode());
        result = result * PRIME + (this.isBool() ? 79 : 97);
        final Object $aDouble = this.getADouble();
        result = result * PRIME + ($aDouble == null ? 43 : $aDouble.hashCode());
        result = result * PRIME + this.getPrInt();
        result = result * PRIME + this.getAChar();
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Pojo;
    }

    public String toString() {
        return "com.vshatrov.entities.Pojo(i1=" + this.getI1() + ", str=" + this.getStr() + ", list=" + this.getList() + ", bool=" + this.isBool() + ", aDouble=" + this.getADouble() + ", prInt=" + this.getPrInt() + ", aChar=" + this.getAChar() + ")";
    }
}
