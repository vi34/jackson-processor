package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class Collections {
    public int[] ints;
    public Pojo[] pojos;
    public List<Integer> lInts;
    public List<Pojo> lPojos;
    public Enums.En[] ens;
    public Set<Integer> integerSet;
    public EnumSet<Enums.En> enumSet;

    public Collections(int[] ints, Pojo[] pojos, List<Integer> lInts, List<Pojo> lPojos,
                       Enums.En[] ens, Set<Integer> integerSet, EnumSet<Enums.En> enumSet) {
        this.ints = ints;
        this.pojos = pojos;
        this.lInts = lInts;
        this.lPojos = lPojos;
        this.ens = ens;
        this.integerSet = integerSet;
        this.enumSet = enumSet;
    }

    public Collections() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Collections that = (Collections) o;

        if (!Arrays.equals(ints, that.ints)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(pojos, that.pojos)) return false;
        if (lInts != null ? !lInts.equals(that.lInts) : that.lInts != null) return false;
        if (lPojos != null ? !lPojos.equals(that.lPojos) : that.lPojos != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(ens, that.ens)) return false;
        if (integerSet != null ? !integerSet.equals(that.integerSet) : that.integerSet != null) return false;
        return enumSet != null ? enumSet.equals(that.enumSet) : that.enumSet == null;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(ints);
        result = 31 * result + Arrays.hashCode(pojos);
        result = 31 * result + (lInts != null ? lInts.hashCode() : 0);
        result = 31 * result + (lPojos != null ? lPojos.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(ens);
        result = 31 * result + (integerSet != null ? integerSet.hashCode() : 0);
        result = 31 * result + (enumSet != null ? enumSet.hashCode() : 0);
        return result;
    }
}
