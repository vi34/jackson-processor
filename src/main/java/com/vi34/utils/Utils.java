package com.vi34.utils;

/**
 * Created by vi34 on 15/04/2017.
 */
public class Utils {
    public static String qualifiedToSimple(String typeName) {
        int dot = typeName.lastIndexOf('.');
        return dot == -1 ? typeName : typeName.substring(dot + 1);
    }
}
