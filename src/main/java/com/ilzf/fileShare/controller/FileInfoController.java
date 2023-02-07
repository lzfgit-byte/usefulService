package com.ilzf.fileShare.controller;

import cn.hutool.core.io.FileUtil;
import com.ilzf.base.entity.ResultEntity;
import com.ilzf.utils.FileUtilILZF;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/file")
public class FileInfoController {

    @RequestMapping("/uploadFile")
    public ResultEntity<?> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        if (file == null){
            return ResultEntity.error();
        }
        String name = file.getOriginalFilename();
        assert name != null;
        String[] split = name.split("\\.");
        if(!(split.length > 0 && "pdf".equals(split[split.length-1]))){
            return ResultEntity.error("请上传pdf文件");
        }
        File target = null;
        try {
            String filePath = FileUtilILZF.getFileBasePath() + name;
            target = new File(filePath);
            if(target.exists() && !target.delete()){
                return ResultEntity.error("文件已存在");
            }
            file.transferTo(target);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.error();
        }
        return ResultEntity.success();
    }
}
