package com.ilzf.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.UserInfo;

import java.io.File;

public class FileUtilILZF {
    public static String getFileBasePath() {
        UserInfo userInfo = SystemUtil.getUserInfo();
        return userInfo.getCurrentDir();
    }
    public static String getUploadFilePath() {
        String res = getFileBasePath() + "files\\";
        File dir = new File(res);
        int count = 0;
        if (!dir.exists()) {
            while (count < 10 && !dir.mkdir()) {
                count++;
            }
        }
        return res;
    }
    public static String getSaveDataPath() {
        String res = getFileBasePath() + "db\\";
        File dir = new File(res);
        int count = 0;
        if (!dir.exists()) {
            while (count < 10 && !dir.mkdir()) {
                count++;
            }
        }
        return res;
    }
    public static boolean writeFile(String path, File file){
        return true;
    }


    public static void main(String[] args) {

        System.out.println(getFileBasePath());
    }
}