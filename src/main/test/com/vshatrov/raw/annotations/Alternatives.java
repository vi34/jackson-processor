package com.vshatrov.raw.annotations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vshatrov.annotations.AlternativeNames;
import com.vshatrov.raw.Pojo;
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
public class Alternatives {

    @AlternativeNames({"fullname", "Name", "nAme"})
    public String name;
    @AlternativeNames({"old"})
    public int age;
    @AlternativeNames("Pojjo")
    public Pojo pojo;
}
