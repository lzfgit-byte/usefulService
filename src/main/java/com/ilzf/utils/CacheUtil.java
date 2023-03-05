package com.ilzf.utils;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class CacheUtil {

    public static String getSaveCacheKey(String url, String salt) {
        if (StringUtilIZLF.isNotBlankOrEmpty(salt)) {
            return StringUtilIZLF.md5(url) + salt;
        }
        return StringUtilIZLF.md5(url);
    }

    public static boolean hasCache(String key) {
        File file = new File(FileUtilILZF.getTempFilePath() + key);
        return file.exists();
    }

    public static String readStrCache(String key) {
        File file = new File(FileUtilILZF.getTempFilePath() + key);
        return FileUtil.readString(file, StandardCharsets.UTF_8);
    }

    public static byte[] readByteCache(String key) {
        File file = new File(FileUtilILZF.getTempFilePath() + key);
        return FileUtil.readBytes(file);
    }

    public static void setCache(String key, String value) {
        File file = new File(FileUtilILZF.getTempFilePath() + key);
        FileUtil.writeString(value, file, StandardCharsets.UTF_8);
    }

    public static void setCache(String key, byte[] value) {
        File file = new File(FileUtilILZF.getTempFilePath() + key);
        FileUtil.writeBytes(value, file);
    }

}
