package com.vshatrov.raw.annotations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vshatrov.annotations.OldProperty;
import com.vshatrov.raw.Pojo;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class PrimitiveTypeChange {

    //@JsonFilter("")
    public String name;

    @OldProperty("i1")
    public Pojo pojo;

    public PrimitiveTypeChange(String name, Pojo pojo) {
        this.name = name;
        this.pojo = pojo;
    }

    public PrimitiveTypeChange() {
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PrimitiveTypeChange)) return false;
        final PrimitiveTypeChange other = (PrimitiveTypeChange) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$pojo = this.pojo;
        final Object other$pojo = other.pojo;
        if (this$pojo == null ? other$pojo != null : !this$pojo.equals(other$pojo)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $pojo = this.pojo;
        result = result * PRIME + ($pojo == null ? 43 : $pojo.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PrimitiveTypeChange;
    }

    public String toString() {
        return "com.vshatrov.raw.annotations.TypeChange(name=" + this.name + ", pojo=" + this.pojo + ")";
    }
}
