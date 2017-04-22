package com.vshatrov.raw;

import com.vshatrov.annotations.GenerateClasses;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Viktor Shatrov.
 */
@GenerateClasses
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Resolve {
    public String name;
    public UnknownClass unknownClass;
}
