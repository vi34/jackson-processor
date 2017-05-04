package com.vshatrov.raw.annotations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Renaming {
    @JsonProperty("first")
    public int i1;
    @JsonProperty("second")
    public double a2;

    private String surname;

    @JsonProperty("surname")
    public String getName() {
        return surname;
    }

    @JsonProperty("surname")
    public void setName(String name) {
        surname = name;
    }
}
