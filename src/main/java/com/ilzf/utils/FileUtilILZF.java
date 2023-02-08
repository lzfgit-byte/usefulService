package com.ilzf.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.UserInfo;

import java.io.File;

public class FileUtilILZF {
    //存储上传文件的位置
    public static final String FILE_PATH = "file";
    //存储数据存储的位置
    public static final String DB = "db";
    //配置文件位置
    public static final String CONFIG_PATH = "config.json";

    /**
     * 获取运行环境的跟目录，没有则创建
     *
     * @return
     */
    public static String getFileBasePath() {
        UserInfo userInfo = SystemUtil.getUserInfo();
        return userInfo.getCurrentDir();
    }

    public static String getUploadFilePath() {
        String res = getFileBasePath() + FILE_PATH + "\\";
        File dir = new File(res);
        int count = 0;
        if (!dir.exists()) {
            while (count < 10 && !dir.mkdir()) {
                count++;
            }
        }
        return res;
    }

    /**
     * 获取运保存数据的根目录，没有则创建
     *
     * @return
     */
    public static String getSaveDataPath() {
        String res = getFileBasePath() + DB + "\\";
        File dir = new File(res);
        int count = 0;
        if (!dir.exists()) {
            while (count < 10 && !dir.mkdir()) {
                count++;
            }
        }
        return res;
    }

    /**
     * 获取配置文件的路径
     *
     * @return
     */
    public static String getConfigPath() {
        return getFileBasePath() + CONFIG_PATH;
    }

    public static boolean writeFile(String path, File file) {
        return true;
    }


    public static void main(String[] args) {

        System.out.println(getFileBasePath());
    }
}