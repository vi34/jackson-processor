package com.vi34.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Created by vi34 on 13/08/16.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Pojo {
    @JsonProperty("i1") int i1;
    @JsonProperty("Str") String str;
    @JsonProperty("Ilist") List<Integer> list;
    boolean bool;
    Double aDouble;
    private int prInt;
    Character aChar;
}
