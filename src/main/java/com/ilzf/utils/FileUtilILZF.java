package com.ilzf.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.UserInfo;
import com.ilzf.fileShare.entity.FileInfoEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class FileUtilILZF {
    //存储上传文件的位置
    public static final String FILE_PATH = "files";
    //存储数据存储的位置
    public static final String DB = "db";
    //配置文件位置
    public static final String CONFIG_PATH = "config.json";
    //缓存位置
    public static final String TEMP = "temp";
    //分隔符
    public static final String SEPARATOR = "\\";

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
        String res = getFileBasePath() + FILE_PATH + SEPARATOR;
        File dir = new File(res);
        int count = 0;
        if (!dir.exists()) {
            while (count < 10 && !dir.mkdir()) {
                count++;
            }
        }
        return res;
    }

    public static String getTempFilePath() {
        String res = getFileBasePath() + TEMP + SEPARATOR;
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
        String res = getFileBasePath() + DB + SEPARATOR;
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

    public static void downloadFileToClient(String filename, File file, HttpServletResponse response) {

        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(file);
            byte[] buf = new byte[4096];
            int readLength;
            setResponseHeader(response, filename, file);
            while (((readLength = inStream.read(buf)) != -1)) {
                response.getOutputStream().write(buf, 0, readLength);
            }
        } catch (Exception e) {
        } finally {
            try {
                inStream.close();
            } catch (Exception e) {

            }
        }
    }

    protected static void setResponseHeader(HttpServletResponse response, String fileName, File file) {
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

    /**
     * 获取磁盘信息
     *
     * @return
     */
    public static List<FileInfoEntity> getDisk() {
        // 操作系统
        List<FileInfoEntity> list = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            String dirName = c + ":" + SEPARATOR;
            File win = new File(dirName);
            if (win.exists()) {
//                double total = win.getTotalSpace() / 1024.0 / 1024.0 / 1024.0;
//                double free = win.getFreeSpace() / 1024.0 / 1024.0 / 1024.0;
//                // 保留一位小数
//                total = Double.valueOf(String.valueOf(total).substring(0, String.valueOf(total).indexOf(".") + 2));
//                free = Double.valueOf(String.valueOf(free).substring(0, String.valueOf(free).indexOf(".") + 2));
//                Double compare = (Double) (1 - free * 1.0 / total) * 100;
//                String str = c + ":盘  总量:" + total + "G 剩余  " + free + "G 已使用 " + compare.intValue() + "%";
                list.add(new FileInfoEntity(dirName.toUpperCase(), dirName.toUpperCase(), false));
            }
        }
        return list;
    }

    public static void walkFiles(File file, Consumer<File> consumer) {
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (ArrayUtil.isNotEmpty(subFiles)) {
                Arrays.stream(subFiles).forEach(consumer);
            }
        } else {
            consumer.accept(file);
        }
    }

    public static void main(String[] args) {

        System.out.println(getFileBasePath());
    }
}