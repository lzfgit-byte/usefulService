package com.ilzf.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
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


    public static String readStrFormStream(InputStream is) throws Exception {
        byte[] data;
        byte[] bytes = new byte[1000];
        int count = 0;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            while ((count = is.read(bytes)) > -1){
                output.write(bytes,0,count);
            }
            data = output.toByteArray();
            return new String(data, StandardCharsets.UTF_8);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        throw new Exception("转换失败");
    }
}
