package com.vi34.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vi34.annotations.Json;
import lombok.*;

import java.util.ArrayList;
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
public class PrivateComplex {
    private String name;
    private int num;
    private List<PrivatePojo> list;

    public static PrivateComplex makeComplex(int lsize) {
        List<PrivatePojo> list = new ArrayList<>();
        for (int i = 0; i < lsize; ++i) {
            list.add(PrivatePojo.makePrivatePojo());
        }
        return new PrivateComplex("privateComplex", lsize, list);
    }
}
