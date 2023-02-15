package com.ilzf.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ilzf.base.annotation.Table;
import com.ilzf.base.annotation.Unique;
import com.ilzf.fileShare.entity.FileInfoEntity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将文件基本信息 存储到本地
 * 将table的名字创建一个文件，然后将新的信息存进去
 */
public class DataUtilILZF {
    public static final String DB_SUFFIX = ".data";

    /**
     * 获取类数据的map结构
     *
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> getEntityMapData(T entity) {
        Class<?> aClass = entity.getClass();
        Field[] fields = aClass.getDeclaredFields();
        Map<String, Object> tempData = new HashMap<>();
        for (Field field : fields) {
            Class<?> type = field.getType();
            String name = field.getName();
            if (String.class == type) {
                try {
                    Method getMethod = aClass.getMethod("get" + StringUtilIZLF.upFirstCharCode(name));
                    Object invoke = getMethod.invoke(entity);
                    assert invoke != null;
                    String s = invoke.toString();
                    tempData.put(name, s);
                } catch (Exception e) {
                    LogUtilILZF.log("数据解析失败" + e.getMessage());
                    e.printStackTrace();
                }
            } else if (Long.class == type) {
                try {
                    Method getMethod = aClass.getMethod("get" + StringUtilIZLF.upFirstCharCode(name));
                    Object invoke = getMethod.invoke(entity);
                    assert invoke != null;
                    tempData.put(name, invoke);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtilILZF.log("数据解析失败" + e.getMessage());
                }
            }

        }
        return tempData;
    }

    public static String getUniqueFieldList(Class<?> aClass) {
        String res = null;
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            Unique unique = field.getAnnotation(Unique.class);
            if (unique != null) {
                res = name;
            }
        }
        return res;
    }

    public static JSONObject getSavedData(Class<?> cla) {
        Table tableValue = cla.getDeclaredAnnotation(Table.class);
        String value = tableValue.value();
        String tablePath = FileUtilILZF.getSaveDataPath() + value + DB_SUFFIX;
        File file = new File(tablePath);
        String dataStr = FileUtil.readString(file, StandardCharsets.UTF_8);
        return JSONUtil.parseObj(dataStr);
    }

    /**
     * 存储文件
     *
     * @param tableName
     * @param data
     * @return
     */
    public static boolean doSave(String tableName, Map<String, Object> data, Class<?> cla) {
        String uniqueField = getUniqueFieldList(cla);
        String uniqueId = StringUtilIZLF.wrapperString(data.get(uniqueField));
        if (StringUtilIZLF.isBlankOrEmpty(uniqueId)) {
            LogUtilILZF.log("空唯一值");
            return false;
        }
        String tablePath = FileUtilILZF.getSaveDataPath() + tableName + DB_SUFFIX;
        File file = new File(tablePath);
        if (!file.exists()) {
            try {
                boolean newFile = file.createNewFile();
                if (!newFile) {
                    LogUtilILZF.log("文件创建失败");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String dataStr = FileUtil.readString(file, StandardCharsets.UTF_8);
        if (StringUtilIZLF.isBlankOrEmpty(dataStr)) {
            dataStr = "{}";
        }
        JSONObject obj = JSONUtil.parseObj(dataStr);
        obj.append(uniqueId, data);
        String s = JSONUtil.toJsonStr(obj, 2);
        FileUtil.writeBytes(s.getBytes(), tablePath);
        LogUtilILZF.log("记录行数据添加[" + tableName + "]");
        return true;
    }

    public static <T> boolean saveData(T entity) {
        Class<?> aClass = entity.getClass();
        Table tableValue = aClass.getDeclaredAnnotation(Table.class);
        String value = tableValue.value();
        Map<String, Object> tempData = getEntityMapData(entity);
        return doSave(value, tempData, aClass);
    }


    public static void main(String[] args) {
        saveData(new FileInfoEntity(new File("D:\\project\\usefulService\\files\\ufulService.zip")));
    }
}
