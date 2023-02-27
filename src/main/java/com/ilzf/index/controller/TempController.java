package com.ilzf.index.controller;

import com.ilzf.base.annotation.RequestBodyJson;
import com.ilzf.base.entity.ResultEntity;
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
            }else {
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
}
