package com.vi34.raw;

import com.vi34.annotations.GenerateClasses;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * Created by vi34 on 03/04/2017.
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ComplexStructures {
    public List<List<Integer>> lInts;
    //public Map<List<Integer>, List<String>> listListMap;
}
