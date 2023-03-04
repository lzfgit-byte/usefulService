package com.ilzf.index.controller;

import cn.hutool.json.JSONObject;
import com.ilzf.base.annotation.RequestBodyJson;
import com.ilzf.base.entity.ResultEntity;
import com.ilzf.index.entity.SaveSomeData;
import com.ilzf.utils.DataUtilILZF;
import com.ilzf.utils.FileUtilILZF;
import com.ilzf.utils.LogUtilILZF;
import com.ilzf.utils.StringUtilIZLF;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/temp")
public class TempController {

    @RequestMapping("/clearTemp")
    public ResultEntity<?> clearTemp(@RequestBodyJson("type") String type) {
        String tempPath = FileUtilILZF.getTempFilePath();
        File file = new File(tempPath);
        List<Boolean> allDelete = new ArrayList<>();

        FileUtilILZF.walkFiles(file, file1 -> {
            if (StringUtilIZLF.isNotBlankOrEmpty(type) && file1.getName().contains(type)) {
                boolean delete = file1.delete();
                allDelete.add(delete);
                if (!delete) LogUtilILZF.log("文件【", file1.getName(), "】", "删除失败");
            } else {
                boolean delete = file1.delete();
                allDelete.add(delete);
                if (!delete) LogUtilILZF.log("文件【", file1.getName(), "】", "删除失败");
            }

        });
        AtomicInteger thy = new AtomicInteger();
        AtomicInteger fty = new AtomicInteger();
        allDelete.forEach(item -> {
            int i = item ? thy.getAndIncrement() : fty.getAndIncrement();
        });
        if (fty.get() == 0) {
            return ResultEntity.success();
        }
        return ResultEntity.error("部分失败");
    }

    @RequestMapping("/getSaveData")
    public ResultEntity<?> getSaveData(@RequestBodyJson("key") String key) {
        JSONObject savedData = DataUtilILZF.getSavedData(SaveSomeData.class);
        return ResultEntity.success(StringUtilIZLF.wrapperString(savedData.get(key)));
    }

    @RequestMapping("/doSaveData")
    public ResultEntity<?> doSaveData(@RequestBodyJson("key") String key, @RequestBodyJson("setting") String setting) {
        SaveSomeData saveSomeData = new SaveSomeData(key, setting);
        DataUtilILZF.saveData(saveSomeData);
        return ResultEntity.success();
    }

}
