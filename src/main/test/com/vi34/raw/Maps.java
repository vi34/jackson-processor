package com.vi34.raw;

import com.vi34.annotations.GenerateClasses;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by vi34 on 03/04/2017.
 */
@GenerateClasses
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class Maps {
    private Map<String, String> props;
    private HashMap<Integer, String> hashMap;
    private TreeMap<String, Pojo> treeMap;
}
