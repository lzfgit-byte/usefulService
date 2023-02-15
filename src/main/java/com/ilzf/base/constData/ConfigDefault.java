package com.ilzf.base.constData;

public class ConfigDefault {
    public static final int PROXY_PORT = 10808;
    public static final String PROXY_IP = "127.0.0.1";
    public static final String NEED_PROXY = "1";



    public static boolean getBoolean(String val){
        return "1".equals(val);
    }

}
