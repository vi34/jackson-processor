package com.vi34.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vi34.annotations.Json;
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
@Json
public class Pojo2 {
    @JsonProperty("c") int a;
    @JsonProperty("S") String s;
    @JsonProperty("Flist") List<Integer> list;
    boolean bool;
    Double aDouble;
    private int prInt;
    char aChar;
}
