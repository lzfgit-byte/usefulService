package com.ilzf.utils;

import cn.hutool.setting.dialect.Props;
import lombok.SneakyThrows;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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
            Enumeration<InetAddress> addresses=ni.getInetAddresses();
            while(addresses.hasMoreElements()){
                ip = addresses.nextElement();
                if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(':') == -1) {
                    result.add(ip.getHostAddress());
                }
            }
        }
        return result.stream().map(item -> HTTP + item + SEPARATOR + PORT).collect(Collectors.toList());
    }

}
