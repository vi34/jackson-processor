package com.vshatrov.raw.annotations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vshatrov.processor.annotations.OldProperty;
import com.vshatrov.raw.Complex;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class ObjectTypeChange {

    public String name;

    @OldProperty("pojo")
    public Complex object;

    public ObjectTypeChange(String name, Complex complex) {
        this.name = name;
        this.object = complex;
    }

    public ObjectTypeChange() {
    }

    @Override
    public String toString() {
        return "ObjectTypeChange{" +
                "name='" + name + '\'' +
                ", object=" + object +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectTypeChange that = (ObjectTypeChange) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return object != null ? object.equals(that.object) : that.object == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }
}
