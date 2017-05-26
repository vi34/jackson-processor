package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.*;

import static com.vshatrov.Compilation.random;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class ComplexStructures {
    public List<List<Integer>> lInts;
    public Map<String, Map<Integer, String>> stringMapMap;

    // Not implemented yet. not works natively with standard jackson deserializer
    //public Map<List<Integer>, List<String>> listListMap;



    public ComplexStructures(List<List<Integer>> lInts, Map<String, Map<Integer, String>> stringMapMap) {
        this.lInts = lInts;
        this.stringMapMap = stringMapMap;
    }

    public ComplexStructures() {
    }

    public static ComplexStructures make() {
        List<List<Integer>> llints = Arrays.asList(Arrays.asList(1,23,4)
                , Arrays.asList(23,1234,5,5345)
                , Arrays.asList(1,23,4));

        Map<String, Map<Integer, String>> stringMapMap = new TreeMap<>();
        Map<Integer, String> m1 = new HashMap<>();
        Map<Integer, String> m2 = new HashMap<>();

        stringMapMap.put("one", m1);
        stringMapMap.put("two", m2);

        m1.put(1, "o");
        m1.put(234, "234");
        m1.put(12334, "1324");
        m2.put(3, "3");
        m2.put(4, "4");

        return new ComplexStructures(llints, stringMapMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplexStructures that = (ComplexStructures) o;

        if (lInts != null ? !lInts.equals(that.lInts) : that.lInts != null) return false;
        return stringMapMap != null ? stringMapMap.equals(that.stringMapMap) : that.stringMapMap == null;
    }

    @Override
    public int hashCode() {
        int result = lInts != null ? lInts.hashCode() : 0;
        result = 31 * result + (stringMapMap != null ? stringMapMap.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ComplexStructures{" +
                "lInts=" + lInts +
                ", stringMapMap=" + stringMapMap +
                '}';
    }
}
