package com.ilzf.fileShare.controller;

import cn.hutool.core.io.FileUtil;
import com.ilzf.base.entity.ResultEntity;
import com.ilzf.fileShare.entity.FileInfoEntity;
import com.ilzf.utils.DataUtilILZF;
import com.ilzf.utils.FileUtilILZF;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
        File target;
        try {
            String filePath = FileUtilILZF.getUploadFilePath() + name;
            target = new File(filePath);
            if(FileUtil.exist(target)){
                return ResultEntity.error("文件已存在");
            }
            file.transferTo(target);
            FileInfoEntity fileInfoEntity = new FileInfoEntity(file,filePath);
            boolean b = DataUtilILZF.saveData(fileInfoEntity);
            if(!b){
                return ResultEntity.error("保存错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.error();
        }
        return ResultEntity.success();
    }

    @RequestMapping("/downloadFile")
    public void downloadFile(String IdOrName, HttpServletResponse response) {

    }
}
