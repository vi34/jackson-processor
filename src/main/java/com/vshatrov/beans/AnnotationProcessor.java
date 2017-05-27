package com.vshatrov.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.vshatrov.annotations.AlternativeNames;
import com.vshatrov.annotations.OldProperty;
import com.vshatrov.beans.properties.Property;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Viktor Shatrov.
 */
public class AnnotationProcessor {


    public void processClassAnnotations(TypeElement element, BeanDescription definition) {
        JsonPropertyOrder order = element.getAnnotation(JsonPropertyOrder.class);
        if (order != null) {
            if (order.alphabetic()) {
                definition.getProps().sort(Comparator.comparing(Property::getName));
            } else {
                definition.getProps().sort(Comparator.comparing(Property::getName, (o1, o2) -> {
                    int i1 = Arrays.binarySearch(order.value(), o1);
                    int i2 = Arrays.binarySearch(order.value(), o2);
                    if (i1 < 0) i1 = -1;
                    if (i2 < 0) i2 = -1;
                    return i1 - i2;
                }));
            }
        }
    }

    /**
     * Process property annotations before property added to BeanDescription.
     * @returns null if property must be ignored.
     */
    public Property processFieldAnnotations(VariableElement member, Property property) {

        if (member.getAnnotation(JsonIgnore.class) != null && member.getAnnotation(JsonIgnore.class).value()) {
            return null;
        }

        JsonProperty jsonProperty = member.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            property.setName(jsonProperty.value());
        }

        AlternativeNames alternativeNames = member.getAnnotation(AlternativeNames.class);
        if (alternativeNames != null) {
            property.setAlternativeNames(Arrays.asList(alternativeNames.value()));
        }

        OldProperty oldProperty = member.getAnnotation(OldProperty.class);
        if (oldProperty != null) {
            property.setOldProperty(oldProperty.value());
        }
        return property;
    }
}
