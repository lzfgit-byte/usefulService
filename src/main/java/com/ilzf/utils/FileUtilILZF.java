package com.ilzf.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.UserInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class FileUtilILZF {
    //存储上传文件的位置
    public static final String FILE_PATH = "files";
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

    public static void downloadFileToClient(String filename,  File file, HttpServletResponse response) {

        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(file);
            byte[] buf = new byte[4096];
            int readLength;
            setResponseHeader(response, filename,file);
            while (((readLength = inStream.read(buf)) != -1)) {
                response.getOutputStream().write(buf, 0, readLength);
            }
        }catch (Exception e){
        }finally {
            try {
                inStream.close();
            } catch (Exception e) {

            }
        }
    }
    protected static void setResponseHeader(HttpServletResponse response, String fileName,File file) {
        String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        String prefix = fileName.substring(0, fileName.lastIndexOf("."));
        try {
            response.reset();
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(prefix.getBytes("GB2312"), "8859_1")
                    + suffix);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            response.setHeader("Content-Length", String.valueOf(file.length()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    protected static void downloadNetFile(String source, String dest) {
        // 下载网络文件
//		int bytesum = 0;
        int byteread = 0;
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            URL url = new URL(source);

            URLConnection conn = url.openConnection();
            inStream = conn.getInputStream();
            fs = new FileOutputStream(dest);
            byte[] buffer = new byte[inStream.available()];
//			int length;
            while ((byteread = inStream.read(buffer)) != -1) {
//				bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fs) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {

        System.out.println(getFileBasePath());
    }
}