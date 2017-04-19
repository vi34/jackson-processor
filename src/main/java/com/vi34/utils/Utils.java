package com.vi34.utils;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Created by vi34 on 15/04/2017.
 */
public class Utils {
    public static String qualifiedToSimple(String typeName) {
        String erasure = typeName.split("[\\[<]")[0];
        int dot = erasure.lastIndexOf('.');
        return dot == -1 ? erasure : erasure.substring(dot + 1);
    }

    public static void warning(Messager messager, Element e, String msg, Object... args) {
        if (msg != null) {
            messager.printMessage(
                    Diagnostic.Kind.WARNING,
                    String.format(msg, args),
                    e);
        }
    }
}
