package com.ilzf.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.dialect.Props;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class NetUtilILZF {
    public static final String PORT = StringUtilIZLF.wrapperString(new Props("application.yml").get("port"));
    public static final String HTTP = "http://";
    public static final String SEPARATOR = ":";

    @SneakyThrows
    public static List<String> getLocalhost() {
        List<String> result = new ArrayList<>();
        Enumeration<NetworkInterface> netInterfaces;
        netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip;
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> addresses = ni.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = addresses.nextElement();
                if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(':') == -1) {
                    result.add(ip.getHostAddress());
                }
            }
        }
        return result.stream().map(item -> HTTP + item + SEPARATOR + PORT).collect(Collectors.toList());
    }

    private static HttpURLConnection getHttpURLConnection(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection httpUrlConn = null;
        if (ConfigUtilILZF.ConfigDefault.NEED_PROXY) {
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(ConfigUtilILZF.ConfigDefault.PROXY_IP, ConfigUtilILZF.ConfigDefault.PROXY_PORT));
            httpUrlConn = (HttpURLConnection) url.openConnection(proxy);
        } else {
            httpUrlConn = (HttpURLConnection) url.openConnection();
        }
        httpUrlConn.setDoInput(true);
        httpUrlConn.setRequestMethod("GET");
        httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        return httpUrlConn;
    }

    public static String getHtmlByUrl(String urlStr) {

        String tempName = StringUtilIZLF.md5(urlStr);
        File file = new File(FileUtilILZF.getTempFilePath() + tempName + "-html");
        //建立连接
        StringBuffer sb = new StringBuffer();
        try {
            if (file.exists()) {
                return FileUtil.readString(file, StandardCharsets.UTF_8);
            }
            HttpURLConnection httpUrlConn = getHttpURLConnection(urlStr);
            //获取输入流
            InputStream input = httpUrlConn.getInputStream();
            //将字节输入流转换为字符输入流
            InputStreamReader read = new InputStreamReader(input, StandardCharsets.UTF_8);
            //为字符输入流添加缓冲
            BufferedReader br = new BufferedReader(read);
            // 读取返回结果
            String data = br.readLine();
            while (data != null) {
                sb.append(data);
                data = br.readLine();
            }
            FileUtil.writeString(sb.toString(), file, StandardCharsets.UTF_8);
            // 释放资源
            br.close();
            read.close();
            input.close();
            httpUrlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static void getByteFromNet(String url, HttpServletResponse response) {
        String tempName = StringUtilIZLF.md5(url);
        File file = new File(FileUtilILZF.getTempFilePath() + tempName);
        File fileHead = new File(FileUtilILZF.getTempFilePath() + tempName + "-head");
        boolean fileExist = file.exists();
        try (OutputStream output = response.getOutputStream()) {
            //先判断是不是存在缓存
            if (fileExist) {
                byte[] bytes = FileUtil.readBytes(file);
                String str = FileUtil.readString(fileHead, StandardCharsets.UTF_8);
                JSONObject object = JSONUtil.parseObj(str);
                Set<String> keySet = object.keySet();
                object.set("Modified", new Date().toString());
                object.set("date", new Date().toString());
                keySet.forEach(key -> {
                    response.setHeader(key, StringUtilIZLF.wrapperString(object.get(key)));
                });

                output.write(bytes);
                output.flush();
                return;
            }
            OutputStream osFile = Files.newOutputStream(file.toPath());

            HttpURLConnection httpUrlConn = getHttpURLConnection(url);
            //获取输入流
            InputStream input = httpUrlConn.getInputStream();

            Map<String, List<String>> headerFields = httpUrlConn.getHeaderFields();
            Set<String> keySet = headerFields.keySet();
            JSONObject json = new JSONObject();
            keySet.forEach(key -> {
                List<String> values = headerFields.get(key);
                if (values.size() == 1) {
                    response.setHeader(key, values.get(0));
                    json.set(key, values.get(0));
                }
            });

            byte[] a = new byte[1000];
            int count = 0;
            while ((count = input.read(a)) > -1) {
                output.write(a, 0, count);
                osFile.write(a, 0, count);
            }
            input.close();
            httpUrlConn.disconnect();
            output.flush();
            osFile.flush();
            osFile.close();
            FileUtil.writeString(json.toString(), fileHead, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(getHtmlByUrl("https://thehentaiworld.com/?new"));
    }
}
