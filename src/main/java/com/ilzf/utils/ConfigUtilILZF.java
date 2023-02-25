package com.ilzf.utils;


import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

public class ConfigUtilILZF {

    public final static String TRUTH_VALUE = "true";
    public final static String FALSITY_VALUE = "false";
    public final static String CONFIG_PATH = FileUtilILZF.getConfigPath();

    @SneakyThrows
    public static JSONObject loadConfig(){
        File file = new File(CONFIG_PATH);
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
            if(!newFile){
                LogUtilILZF.log("创建配置文件失败");
            }
        }
        String jsonStr = FileUtil.readString(file, StandardCharsets.UTF_8);
        if (StringUtilIZLF.isBlankOrEmpty(jsonStr)) {
            jsonStr = "{}";
        }
        return JSONUtil.parseObj(jsonStr);
    }

    public static void set(String key, String value) {
        JSONObject jsonObj = loadConfig();
        jsonObj.set(key, value);
        FileUtil.writeString(jsonObj.toJSONString(2), new File(CONFIG_PATH), StandardCharsets.UTF_8);
    }
    public static void setMap(Map<String,String> map) {
        JSONObject jsonObj = loadConfig();
        Set<String> keySet = map.keySet();
        keySet.forEach(key ->{
            jsonObj.set(key,map.get(key));
        });
        FileUtil.writeString(jsonObj.toJSONString(2), new File(CONFIG_PATH), StandardCharsets.UTF_8);
    }

    public static String get(String key) {
        JSONObject jsonObj = loadConfig();
        return StringUtilIZLF.wrapperString(jsonObj.get(key));
    }

    public static JSONObject listConfig(){
        return loadConfig();
    }

    public static boolean getBoolean(Object o){
        if(StringUtilIZLF.isBlankOrEmpty(o)){
            return false;
        }
        return TRUTH_VALUE.equals(StringUtilIZLF.wrapperString(o));
    }

    /**
     * 默认为false
     * @param key
     * @return
     */
    public static boolean getBooleanByKey(String key){
        String value = get(key);
        return getBoolean(value);
    }
    /**
     * 默认为true
     * @param key
     * @return
     */
    public static boolean getTrueIfNull(String key){
        String s = get(key);
        return !getBoolean(s);
    }

    public static <T> T getDefaultIfValueNull(String key,T value){
        String s = get(key);
        if(StringUtilIZLF.isNotBlankOrEmpty(s)){
            Class<?> aClass = value.getClass();
            if(aClass == String.class){
                return (T) s;
            }else if(aClass == Integer.class){
                return (T)Integer.valueOf(s);
            }
            return (T) s;
        }
        return value;
    }

    public static void main(String[] args) {
        set("text","21313");
        Integer text = getDefaultIfValueNull("text", 132);
        System.out.println(text);
    }
    static class ConfigDefault {
        public static final int PROXY_PORT = 10801;
        public static final String PROXY_IP = "127.0.0.1";
        public static final boolean NEED_PROXY = getTrueIfNull("needProxy");

    }
}
