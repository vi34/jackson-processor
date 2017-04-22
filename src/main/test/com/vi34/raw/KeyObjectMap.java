package com.vi34.raw;

import com.vi34.annotations.GenerateClasses;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * Created by vi34 on 19/04/2017.
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@GenerateClasses
public class KeyObjectMap {
    public Map<Pojo, Pojo> keyObjects;
}
