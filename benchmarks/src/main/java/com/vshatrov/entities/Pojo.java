package com.vshatrov.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vshatrov.annotations.GenerateClasses;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
public class Pojo {
    @JsonProperty("i1") int i1;
    @JsonProperty("Str") String str;
    @JsonProperty("Ilist") List<Integer> list;
    boolean bool;
    Double aDouble;
    private int prInt;
    char aChar;

    static Random random = new Random();

    public static Pojo makePojo() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            list.add(random.nextInt());
        }
        return new Pojo(random.nextInt(), "pojo", list,
                random.nextBoolean(), random.nextDouble(), random.nextInt(), 'a');
    }
}