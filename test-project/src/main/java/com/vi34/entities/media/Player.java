package com.vi34.entities.media;

import com.vi34.annotations.Json;

/**
 * Created by vi34 on 31/03/2017.
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
