package com.vshatrov.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vshatrov on 13/08/16.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonSerialize(using = JsonSerializer.None.class)
@JsonDeserialize(using = JsonDeserializer.None.class)
public class Complex {
    String name;
    int num;
    @JsonProperty("pojos") List<Pojo> list;

    public static Complex makeComplex(int lsize) {
        List<Pojo> list = new ArrayList<>();
        for (int i = 0; i < lsize; ++i) {
            list.add(Pojo.makePojo());
        }
        return new Complex("complex", lsize, list);
    }
}
