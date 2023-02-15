package com.ilzf.utils;


import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class ConfigUtilILZF {
    @SneakyThrows
    public static void set(String key, String value) {
        String configPath = FileUtilILZF.getConfigPath();
        File file = new File(configPath);
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
        JSONObject jsonObj = JSONUtil.parseObj(jsonStr);
        jsonObj.set(key, value);
        FileUtil.writeString(jsonObj.toJSONString(2), file, StandardCharsets.UTF_8);
    }

    public static String get(String key) {
        String configPath = FileUtilILZF.getConfigPath();
        File file = new File(configPath);
        if (!file.exists()) {
            LogUtilILZF.log("没有配置文件");
            return null;
        }
        String jsonStr = FileUtil.readString(file, StandardCharsets.UTF_8);
        if(StringUtilIZLF.isBlankOrEmpty(jsonStr)){
            LogUtilILZF.log("配置文件为空");
            return null;
        }
        JSONObject object = JSONUtil.parseObj(jsonStr);
        return StringUtilIZLF.wrapperString(object.get(key));
    }
}
