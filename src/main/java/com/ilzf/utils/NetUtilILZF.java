package com.ilzf.utils;

import cn.hutool.setting.dialect.Props;
import com.ilzf.base.constData.ConfigDefault;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class NetUtilILZF {
    public static final String PORT = StringUtilIZLF.wrapperString(new Props("application.yml").get("port"));
    public static final String HTTP = "http://";
    public static final String SEPARATOR = ":";
    static {
        Properties prop = System.getProperties();
        prop.put("proxySet", true);
        prop.setProperty("socksProxyHost", ConfigDefault.PROXY_IP);
        prop.setProperty("socksProxyPort", ConfigDefault.PROXY_PORT+"");
    }
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


    @SneakyThrows
    public static String getHtmlByUrl(String urlStr) {
        //建立连接
        URL url = new URL(urlStr);
        HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
        httpUrlConn.setDoInput(true);
        httpUrlConn.setRequestMethod("GET");
        httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //获取输入流
        InputStream input = httpUrlConn.getInputStream();
        //将字节输入流转换为字符输入流
        InputStreamReader read = new InputStreamReader(input, StandardCharsets.UTF_8);
        //为字符输入流添加缓冲
        BufferedReader br = new BufferedReader(read);
        // 读取返回结果
        String data = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (data != null) {
            sb.append(data);
            data = br.readLine();
        }
        // 释放资源
        br.close();
        read.close();
        input.close();
        httpUrlConn.disconnect();
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getHtmlByUrl("https://thehentaiworld.com/?new"));
    }
}
