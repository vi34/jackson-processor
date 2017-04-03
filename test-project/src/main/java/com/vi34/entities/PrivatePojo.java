package com.vi34.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by vi34 on 13/08/16.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class PrivatePojo {
    @JsonProperty("i1")
    private int i1;
    @JsonProperty("Str")
    private String str;
    @JsonProperty("Ilist")
    private List<Integer> list;
    private boolean bool;
    private Double aDouble;
    private int prInt;
    private Character aChar;

    static Random random = new Random();

    public static PrivatePojo makePrivatePojo() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            list.add(random.nextInt());
        }
        return new PrivatePojo(random.nextInt(), "privatePojo", list,
                random.nextBoolean(), random.nextDouble(), random.nextInt(), 'a');
    }
}
