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
    @JsonProperty("c") int a;
    @JsonProperty("S") String s;
    @JsonProperty("Flist") List<Integer> list;
}
