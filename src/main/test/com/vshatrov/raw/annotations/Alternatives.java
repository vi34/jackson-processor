package com.vshatrov.raw.annotations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vshatrov.annotations.AlternativeNames;
import com.vshatrov.raw.Pojo;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class Alternatives {

    @AlternativeNames({"fullname", "Name", "nAme"})
    public String name;
    @AlternativeNames({"old"})
    public int age;
    @AlternativeNames("Pojjo")
    public Pojo pojo;

    @java.beans.ConstructorProperties({"name", "age", "pojo"})
    public Alternatives(String name, int age, Pojo pojo) {
        this.name = name;
        this.age = age;
        this.pojo = pojo;
    }

    public Alternatives() {
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Alternatives)) return false;
        final Alternatives other = (Alternatives) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        if (this.age != other.age) return false;
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
        result = result * PRIME + this.age;
        final Object $pojo = this.pojo;
        result = result * PRIME + ($pojo == null ? 43 : $pojo.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Alternatives;
    }

    public String toString() {
        return "com.vshatrov.raw.annotations.Alternatives(name=" + this.name + ", age=" + this.age + ", pojo=" + this.pojo + ")";
    }
}
