package com.ilzf.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.dialect.Props;
import lombok.SneakyThrows;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
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
        //建立连接
        StringBuffer sb = new StringBuffer();
        try {
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

    public static void getImgByte(String imgUrl, HttpServletResponse response) {
        String[] split = imgUrl.split("\\.");
        String imageType = split[split.length - 1];
        try (OutputStream output = response.getOutputStream()) {
            HttpURLConnection httpUrlConn = getHttpURLConnection(imgUrl);
            //获取输入流
            InputStream input = httpUrlConn.getInputStream();

            Map<String, List<String>> headerFields = httpUrlConn.getHeaderFields();
            Set<String> keySet = headerFields.keySet();
            keySet.forEach(key -> {
                List<String> values = headerFields.get(key);
                if (values.size() == 1) {
                    response.setHeader(key, values.get(0));
                }
            });

            byte[] a = new byte[1000];
            int count = 0;
            while ((count = input.read(a)) > -1) {
                output.write(a, 0, count);
            }
            input.close();
            httpUrlConn.disconnect();
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getVideoByte(String url, HttpServletResponse response) {
        String[] split = url.split("\\.");
        String VideoType = split[split.length - 1];
        try (OutputStream output = response.getOutputStream()) {
            HttpURLConnection httpUrlConn = getHttpURLConnection(url);
            //获取输入流
            InputStream input = httpUrlConn.getInputStream();
            Map<String, List<String>> headerFields = httpUrlConn.getHeaderFields();
            Set<String> keySet = headerFields.keySet();
            keySet.forEach(key -> {
                List<String> values = headerFields.get(key);
                if (values.size() == 1) {
                    response.setHeader(key, values.get(0));
                }
            });


            byte[] a = new byte[1000];
            int count = 0;
            while ((count = input.read(a)) > -1) {
                output.write(a, 0, count);
            }
            input.close();
            httpUrlConn.disconnect();
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(getHtmlByUrl("https://thehentaiworld.com/?new"));
    }
}
