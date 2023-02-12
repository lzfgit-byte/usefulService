package com.ilzf.utils;

public class StringUtilIZLF {
    public static final String LINE_BREAK = "\r";

    public static String upFirstCharCode(String str) {
        String s = str.substring(0, 1);
        String end = str.substring(1);
        return s.toUpperCase() + end;
    }

    public static boolean isBlankOrEmpty(Object o) {
        return o == null || "".equals(o.toString());
    }

    public static boolean isNotBlankOrEmpty(Object o) {
        return !isBlankOrEmpty(o);
    }

    public static String wrapperString(Object o) {
        return o == null ? "" : o.toString();
    }

    public static String breakLLineWord (Object str){
        return wrapperString(str) + LINE_BREAK;
    }
    public static void print(Object str){
        System.out.println(breakLLineWord(str));
    }
}
