package com.vshatrov.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vshatrov.annotations.GenerateClasses;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author vshatrov on 13/08/16.
 */
@AllArgsConstructor
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonSerialize(using = JsonSerializer.None.class)
@JsonDeserialize(using = JsonDeserializer.None.class)
public class Pojo {
    @JsonProperty("i1") int i1;
    @JsonProperty("Str") String str;
    @JsonProperty("Ilist") List<Integer> list;
    boolean bool;
    Double aDouble;
    private int prInt;
    char aChar;

    static Random random = new Random();

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
}
