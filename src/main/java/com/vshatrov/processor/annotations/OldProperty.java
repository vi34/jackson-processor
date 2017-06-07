package com.vshatrov.processor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Value must specify name of property inside class of annotated field, that specify old type of this property.
 * It means that during deserialization, if instead of object there will be found primitive value,
 * value will be assigned to specified property.
 * @author Viktor Shatrov.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface OldProperty {
    String value();
}
