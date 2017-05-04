package com.vshatrov.utils;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import java.util.Optional;

import static javax.lang.model.type.TypeKind.*;

/**
 * @author Viktor Shatrov.
 */
public class Utils {
    public static Types typeUtils;

    public static String qualifiedToSimple(String typeName) {
        String erasure = erasure(typeName);
        int dot = erasure.lastIndexOf('.');
        return dot == -1 ? erasure : erasure.substring(dot + 1);
    }

    public static String erasure(String typeName) {
        return typeName.split("[\\[<]")[0];
    }


    public static void warning(Messager messager, Element e, String msg, Object... args) {
        if (msg != null) {
            messager.printMessage(
                    Diagnostic.Kind.WARNING,
                    String.format(msg, args),
                    e);
        }
    }

    public static boolean isEnum(TypeMirror type) {
        return haveSupertype(type, "java.lang.Enum");
    }

    public static boolean iterable(TypeMirror type) {
        return haveSupertype(type, "java.util.Collection");
    }

    public static boolean isMap(TypeMirror type) {
        return haveSupertype(type, "java.util.Map");
    }

    private static boolean haveSupertype(TypeMirror type, String superType) {
        if (typeUtils.erasure(type).toString().equals(superType)) return true;

        for (TypeMirror directSuperType : typeUtils.directSupertypes(type)) {
            if (haveSupertype(directSuperType, superType))
                return true;
        }
        return false;
    }

    public static boolean isNumber(TypeMirror type) {
        if (haveSupertype(type, "java.lang.Number")) return true;

        TypeKind kind = type.getKind();
        return kind == INT || kind == LONG || kind == SHORT || kind == BYTE || kind == DOUBLE || kind == FLOAT;
    }

    public static Optional<Object> extractAnnotationValue(AnnotationMirror annotation, String key) {
        return annotation.getElementValues().entrySet().stream().filter(p -> p.getKey().toString().equals(key))
                .findAny().map(p -> p.getValue().getValue());
    }

    public static Optional<? extends AnnotationMirror> getAnnotation(Element symbol, Class<?> annType) {
            return symbol.getAnnotationMirrors().stream()
                    .filter(ann -> ann.getAnnotationType().toString().equals(annType.getCanonicalName()))
                    .findAny();
    }
}
