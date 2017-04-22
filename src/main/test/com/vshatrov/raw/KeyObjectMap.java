package com.vshatrov.raw;

import lombok.*;

import java.util.Map;

/**
 * @author Viktor Shatrov.
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
//@GenerateClasses
@NoArgsConstructor
public class KeyObjectMap {
    public Map<Pojo, Pojo> keyObjects;
}
