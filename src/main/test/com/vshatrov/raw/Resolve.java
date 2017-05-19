package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class Resolve {
    public String name;
    public UnknownClass unknownClass;
    public List<UnknownClass> unknownClassList;

    public Resolve(String name, UnknownClass unknownClass, List<UnknownClass> unknownClassList) {
        this.name = name;
        this.unknownClass = unknownClass;
        this.unknownClassList = unknownClassList;
    }

    public Resolve() {
    }

    public static Resolve make() {
        List<UnknownClass> unknownClasses = new ArrayList<>();
        UnknownClass unknown = new UnknownClass(999, "unknown");
        UnknownClass unknown2 = new UnknownClass(999, "unknown");
        unknownClasses.add(unknown);
        unknownClasses.add(unknown2);
        return new Resolve("resolve", unknown, unknownClasses);
    }


    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Resolve)) return false;
        final Resolve other = (Resolve) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$unknownClass = this.unknownClass;
        final Object other$unknownClass = other.unknownClass;
        if (this$unknownClass == null ? other$unknownClass != null : !this$unknownClass.equals(other$unknownClass))
            return false;
        final Object this$unknownClassList = this.unknownClassList;
        final Object other$unknownClassList = other.unknownClassList;
        if (this$unknownClassList == null ? other$unknownClassList != null : !this$unknownClassList.equals(other$unknownClassList))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $unknownClass = this.unknownClass;
        result = result * PRIME + ($unknownClass == null ? 43 : $unknownClass.hashCode());
        final Object $unknownClassList = this.unknownClassList;
        result = result * PRIME + ($unknownClassList == null ? 43 : $unknownClassList.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Resolve;
    }

    public String toString() {
        return "com.vshatrov.raw.Resolve(name=" + this.name + ", unknownClass=" + this.unknownClass + ", unknownClassList=" + this.unknownClassList + ")";
    }
}
