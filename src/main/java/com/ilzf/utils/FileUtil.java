package com.ilzf.utils;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.UserInfo;

import java.io.File;

public class FileUtil {
    public static String getFileBasePath() {
        UserInfo userInfo = SystemUtil.getUserInfo();
        return userInfo.getCurrentDir();
    }
    public static boolean writeFile(String path, File file){
        FileWriter fw = new FileWriter(path);
        return true;
    }


    public static void main(String[] args) {

        System.out.println(getFileBasePath());
    }
}