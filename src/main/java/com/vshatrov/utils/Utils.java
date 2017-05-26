package com.vshatrov.utils;

import com.sun.source.util.Trees;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;
import com.vshatrov.JacksonProcessor;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import java.util.Optional;

import static javax.lang.model.type.TypeKind.*;

/**
 * @author Viktor Shatrov.
 */
public class Utils {
    public static Types typeUtils;
    public static Elements elementUtils;
    public static Filer filer;
    public static Messager messager;
    public static TreeMaker treeMaker;
    public static Trees trees;
    public static Names names;
    public static JavacElements javacElements;

    public static String qualifiedToSimple(String typeName) {
        String erasure = erasure(typeName);
        int dot = erasure.lastIndexOf('.');
        return dot == -1 ? erasure : erasure.substring(dot + 1);
    }

    public static String erasure(String typeName) {
        return typeName.split("[\\[<]")[0];
    }


    public static void warning(Exception e, String msg, Object... args) {
        if (JacksonProcessor.DEBUG) {
            e.printStackTrace();
        }
        if (msg != null) {
            messager.printMessage(
                    Diagnostic.Kind.WARNING,
                    String.format(msg, args),
                    null);
        }
    }

    static int varCounter = 1;
    public static String newVarableName() {
        return "var_" + (varCounter++);
    }

    public static boolean isEnum(TypeMirror type) {
        return hasSupertype(type, "java.lang.Enum");
    }

    public static boolean iterable(TypeMirror type) {
        return hasSupertype(type, "java.util.Collection");
    }

    public static boolean isList(TypeMirror type) {
        return hasSupertype(type, "java.util.List");
    }

    public static boolean isEnumSet(TypeMirror type) {
        return hasSupertype(type, "java.util.EnumSet");
    }

    public static boolean isSet(TypeMirror type) {
        return hasSupertype(type, "java.util.Set");
    }

    public static boolean isMap(TypeMirror type) {
        return hasSupertype(type, "java.util.Map");
    }

    private static boolean hasSupertype(TypeMirror type, String superType) {
        if (typeUtils.erasure(type).toString().equals(superType)) return true;

        for (TypeMirror directSuperType : typeUtils.directSupertypes(type)) {
            if (hasSupertype(directSuperType, superType))
                return true;
        }
        return false;
    }

    public static boolean isNumber(TypeMirror type) {
        if (hasSupertype(type, "java.lang.Number")) return true;

        TypeKind kind = type.getKind();
        return kind == INT || kind == LONG || kind == SHORT || kind == BYTE || kind == DOUBLE || kind == FLOAT;
    }

    public static Optional<? extends AnnotationMirror> getAnnotationMirror(Element symbol, Class<?> annType) {
        return symbol.getAnnotationMirrors().stream()
                    .filter(ann -> ann.getAnnotationType().toString().equals(annType.getCanonicalName()))
                    .findAny();
    }

    public static JCTree.JCIdent ident(String name) {
        return treeMaker.Ident(javacElements.getName(name));
    }
}
