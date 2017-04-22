package com.vshatrov.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vshatrov.annotations.GenerateClasses;
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
@GenerateClasses
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
