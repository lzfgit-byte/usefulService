package com.ilzf.utils;

import cn.hutool.core.io.FileUtil;
import com.ilzf.base.annotation.Table;
import com.ilzf.fileShare.entity.FileInfoEntity;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 将文件基本信息 存储到本地
 * 将table的名字创建一个文件，然后将新的信息存进去
 */
public class DataUtilILZF {
    /**
     * 获取类数据的map结构
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
                    e.printStackTrace();
                }
            } else if (Long.class == type) {
                try {
                    Method getMethod = aClass.getMethod("get" + StringUtilIZLF.upFirstCharCode(name));
                    Object invoke = getMethod.invoke(entity);
                    assert invoke != null;

                    tempData.put(name, (Long) invoke);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return tempData;
    }

    /**
     * 存储文件
     * @param tableName
     * @param data
     * @return
     */
    public static boolean doSave(String tableName, Map<String, Object> data) {
        String tablePath = FileUtilILZF.getSaveDataPath() + tableName;
        String dataStr = FileUtil.readString(tablePath, StandardCharsets.UTF_8);
        return true;
    }

    public static <T> boolean saveData(T entity) {
        Class<?> aClass = entity.getClass();
        Table tableValue = aClass.getDeclaredAnnotation(Table.class);
        String value = tableValue.value();
        Map<String, Object> tempData = getEntityMapData(entity);
        return doSave(value,tempData);
    }


    public static void main(String[] args) {
        saveData(new FileInfoEntity(new File("D:\\project\\usefulService\\files\\usefulService.zip")));
    }
}
