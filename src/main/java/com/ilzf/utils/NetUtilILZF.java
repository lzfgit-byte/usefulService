package com.ilzf.utils;

import cn.hutool.core.net.NetUtil;
import cn.hutool.setting.dialect.Props;

public class NetUtilILZF {
    public static String getLocalhost(){
        String localhostStr = NetUtil.getLocalhostStr();
        Props props = new Props("application.yml");
        return "http://" + localhostStr + ":" + props.get("port");
    }
}
