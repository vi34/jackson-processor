package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Maps {
    public Map<String, String> props;
    public HashMap<Integer, String> hashMap;
    public TreeMap<String, Pojo> treeMap;
}
