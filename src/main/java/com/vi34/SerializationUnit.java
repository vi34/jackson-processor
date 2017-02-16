package com.vi34;

import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * Created by vi34 on 22/12/2016.
 */
@Getter
@Setter
public class SerializationUnit {

    String simpleName;
    TypeElement element;

    SerializationUnit(TypeElement element) {
        this.element = element;
        simpleName = element.getSimpleName().toString();
        inspect();
    }

    void inspect() {
        List<? extends Element> elements = element.getEnclosedElements();
        for (Element element : elements) {

        }
    }
}
