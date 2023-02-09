package com.ilzf.fileShare.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.ilzf.base.entity.ResultEntity;
import com.ilzf.fileShare.entity.FileInfoEntity;
import com.ilzf.utils.DataUtilILZF;
import com.ilzf.utils.FileUtilILZF;
import com.ilzf.utils.StringUtilIZLF;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class FileShareService {
    public ResultEntity<?> uploadFile( MultipartFile file) {
        if (file == null) {
            return ResultEntity.error();
        }
        String name = file.getOriginalFilename();
        assert name != null;
        File target;
        try {
            String filePath = FileUtilILZF.getUploadFilePath() + name;
            target = new File(filePath);
            if (FileUtil.exist(target)) {
                return ResultEntity.error("文件已存在");
            }
            file.transferTo(target);
            FileInfoEntity fileInfoEntity = new FileInfoEntity(file, filePath);
            boolean b = DataUtilILZF.saveData(fileInfoEntity);
            if (!b) {
                return ResultEntity.error("保存错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.error();
        }
        return ResultEntity.success();
    }

    public void downloadFile(String IdOrName, HttpServletResponse response) {
        JSONArray savedData = DataUtilILZF.getSavedData(FileInfoEntity.class);
        AtomicReference<JSONObject> obj = new AtomicReference<>();
        savedData.forEach(item -> {
            JSONObject forObj = (JSONObject) item;
            String id = StringUtilIZLF.wrapperString(forObj.get("id"));
            String name = StringUtilIZLF.wrapperString(forObj.get("name"));
            if(id.equals(IdOrName) || name.equals(IdOrName)){
                obj.set(forObj);
            }
        });
        JSONObject entries = obj.get();
        String path = StringUtilIZLF.wrapperString(entries.get("path"));
        FileReader fileReader = FileReader.create(new File(path));
        try {
            fileReader.writeToStream(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void downloadFilebyPath( String path, HttpServletResponse response) {
        FileReader fileReader = FileReader.create(new File(path));
        try {
            fileReader.writeToStream(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultEntity<List<FileInfoEntity>> listFiles() {
        List<FileInfoEntity> res = new ArrayList<>();
        String uploadFilePath = FileUtilILZF.getUploadFilePath();
        FileUtil.walkFiles(new File(uploadFilePath),file -> {
            res.add(new FileInfoEntity(file));
        });
        return ResultEntity.success(res);
    }
}
