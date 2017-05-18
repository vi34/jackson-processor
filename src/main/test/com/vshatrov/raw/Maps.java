package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class Maps {
    public Map<String, String> props;
    public HashMap<Integer, String> hashMap;
    public TreeMap<String, Pojo> treeMap;

    @java.beans.ConstructorProperties({"props", "hashMap", "treeMap"})
    public Maps(Map<String, String> props, HashMap<Integer, String> hashMap, TreeMap<String, Pojo> treeMap) {
        this.props = props;
        this.hashMap = hashMap;
        this.treeMap = treeMap;
    }

    public Maps() {
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Maps)) return false;
        final Maps other = (Maps) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$props = this.props;
        final Object other$props = other.props;
        if (this$props == null ? other$props != null : !this$props.equals(other$props)) return false;
        final Object this$hashMap = this.hashMap;
        final Object other$hashMap = other.hashMap;
        if (this$hashMap == null ? other$hashMap != null : !this$hashMap.equals(other$hashMap)) return false;
        final Object this$treeMap = this.treeMap;
        final Object other$treeMap = other.treeMap;
        if (this$treeMap == null ? other$treeMap != null : !this$treeMap.equals(other$treeMap)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $props = this.props;
        result = result * PRIME + ($props == null ? 43 : $props.hashCode());
        final Object $hashMap = this.hashMap;
        result = result * PRIME + ($hashMap == null ? 43 : $hashMap.hashCode());
        final Object $treeMap = this.treeMap;
        result = result * PRIME + ($treeMap == null ? 43 : $treeMap.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Maps;
    }

    public String toString() {
        return "com.vshatrov.raw.Maps(props=" + this.props + ", hashMap=" + this.hashMap + ", treeMap=" + this.treeMap + ")";
    }
}
