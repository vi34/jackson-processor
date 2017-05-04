package com.vshatrov.entities.media;

/**
 * @author Viktor Shatrov.
 */
public enum Player {
    JAVA, FLASH;

    public static Player find(String str) {
        if (str == "JAVA") return JAVA;
        if (str == "FLASH") return FLASH;
        if ("JAVA".equals(str)) return JAVA;
        if ("FLASH".equals(str)) return FLASH;
        String desc = (str == null) ? "NULL" : String.format("'%s'", str);
        throw new IllegalArgumentException("No Player value of "+desc);
    }
}
