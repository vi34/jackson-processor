package com.vshatrov.raw;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author Viktor Shatrov.
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
//@JsonSerialize
@NoArgsConstructor
public class ComplexStructures {
    public List<List<Integer>> lInts;
    public Map<List<Integer>, List<String>> listListMap;
    public Map<String, Map<Integer, String>> stringMapMap;
}
