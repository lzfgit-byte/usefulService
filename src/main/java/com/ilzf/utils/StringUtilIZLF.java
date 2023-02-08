package com.ilzf.utils;

public class StringUtilIZLF {
    public static String upFirstCharCode(String str) {
        String s = str.substring(0, 1);
        String end = str.substring(1);
        return s.toUpperCase() + end;
    }
    public static boolean isBlank(Object o){
        return o == null || "".equals(o.toString());
    }
}
