package com.vshatrov.raw;

import com.vshatrov.annotations.GenerateClasses;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Viktor Shatrov.
 */
@GenerateClasses
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Maps {
    public Map<String, String> props;
    public HashMap<Integer, String> hashMap;
    public TreeMap<String, Pojo> treeMap;
}
