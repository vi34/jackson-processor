package com.vshatrov.raw;

import java.util.List;
import java.util.Map;

/**
 * @author Viktor Shatrov.
 */
//@JsonSerialize
public class ComplexStructures {
    public List<List<Integer>> lInts;
    public Map<List<Integer>, List<String>> listListMap;
    public Map<String, Map<Integer, String>> stringMapMap;


    public ComplexStructures(List<List<Integer>> lInts, Map<List<Integer>, List<String>> listListMap, Map<String, Map<Integer, String>> stringMapMap) {
        this.lInts = lInts;
        this.listListMap = listListMap;
        this.stringMapMap = stringMapMap;
    }

    public ComplexStructures() {
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ComplexStructures)) return false;
        final ComplexStructures other = (ComplexStructures) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$lInts = this.lInts;
        final Object other$lInts = other.lInts;
        if (this$lInts == null ? other$lInts != null : !this$lInts.equals(other$lInts)) return false;
        final Object this$listListMap = this.listListMap;
        final Object other$listListMap = other.listListMap;
        if (this$listListMap == null ? other$listListMap != null : !this$listListMap.equals(other$listListMap))
            return false;
        final Object this$stringMapMap = this.stringMapMap;
        final Object other$stringMapMap = other.stringMapMap;
        if (this$stringMapMap == null ? other$stringMapMap != null : !this$stringMapMap.equals(other$stringMapMap))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $lInts = this.lInts;
        result = result * PRIME + ($lInts == null ? 43 : $lInts.hashCode());
        final Object $listListMap = this.listListMap;
        result = result * PRIME + ($listListMap == null ? 43 : $listListMap.hashCode());
        final Object $stringMapMap = this.stringMapMap;
        result = result * PRIME + ($stringMapMap == null ? 43 : $stringMapMap.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ComplexStructures;
    }

    public String toString() {
        return "com.vshatrov.raw.ComplexStructures(lInts=" + this.lInts + ", listListMap=" + this.listListMap + ", stringMapMap=" + this.stringMapMap + ")";
    }
}
