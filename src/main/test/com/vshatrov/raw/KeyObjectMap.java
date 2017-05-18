package com.vshatrov.raw;

import java.util.Map;

/**
 * @author Viktor Shatrov.
 */
//@JsonSerialize
public class KeyObjectMap {
    public Map<Pojo, Pojo> keyObjects;


    @java.beans.ConstructorProperties({"keyObjects"})
    public KeyObjectMap(Map<Pojo, Pojo> keyObjects) {
        this.keyObjects = keyObjects;
    }

    public KeyObjectMap() {
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof KeyObjectMap)) return false;
        final KeyObjectMap other = (KeyObjectMap) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$keyObjects = this.keyObjects;
        final Object other$keyObjects = other.keyObjects;
        if (this$keyObjects == null ? other$keyObjects != null : !this$keyObjects.equals(other$keyObjects))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $keyObjects = this.keyObjects;
        result = result * PRIME + ($keyObjects == null ? 43 : $keyObjects.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof KeyObjectMap;
    }

    public String toString() {
        return "com.vshatrov.raw.KeyObjectMap(keyObjects=" + this.keyObjects + ")";
    }
}
