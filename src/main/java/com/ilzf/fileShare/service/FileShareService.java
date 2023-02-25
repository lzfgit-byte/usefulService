package com.ilzf.fileShare.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import com.ilzf.base.entity.ResultEntity;
import com.ilzf.fileShare.entity.FileInfoEntity;
import com.ilzf.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class FileShareService {
    public ResultEntity<?> uploadFile(MultipartFile file) {
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
        JSONObject savedData = DataUtilILZF.getSavedData(FileInfoEntity.class);
        Set<String> keyS = savedData.keySet();
        AtomicReference<JSONObject> obj = new AtomicReference<>();
        keyS.forEach(key -> {
            JSONObject forObj = (JSONObject) savedData.get(key);
            String id = StringUtilIZLF.wrapperString(forObj.get("id"));
            String name = StringUtilIZLF.wrapperString(forObj.get("name"));
            if (id.equals(IdOrName) || name.equals(IdOrName)) {
                obj.set(forObj);
            }
        });
        JSONObject entries = obj.get();
        String path = StringUtilIZLF.wrapperString(entries.get("path"));
        String name = StringUtilIZLF.wrapperString(entries.get("name"));
        if (StringUtilIZLF.isBlankOrEmpty(path)) {
            LogUtilILZF.log("文件丢失", "[", name, "]");
            return;
        }
        FileUtilILZF.downloadFileToClient(name, new File(path), response);
    }

    public void downloadFilebyPath(String path, HttpServletResponse response) {
        File file = new File(path);
        if (!file.exists()) {
            LogUtilILZF.log("文件丢失", "[", file.getName(), "]");
            return;
        }
        FileUtilILZF.downloadFileToClient(file.getName(), file, response);
    }

    public ResultEntity<List<FileInfoEntity>> listFiles(Map<String, String> map) {
        List<FileInfoEntity> res = new ArrayList<>();
        String path = map.get("path");
        if (StringUtilIZLF.isBlankOrEmpty(path)) {
            res = FileUtilILZF.getDisk();
            return ResultEntity.success(res);
        }

        List<FileInfoEntity> finalRes = res;

        FileUtilILZF.walkFiles(new File(path), file -> {
            if (ConfigUtilILZF.getBooleanByKey("isShowHidden")) {
                if (file.isDirectory()) {
                    finalRes.add(new FileInfoEntity(file.getAbsolutePath(), file.getName(), file.isHidden()));
                } else {
                    finalRes.add(new FileInfoEntity(file));
                }
            } else {
                if (!file.isHidden()) {
                    if (file.isDirectory()) {
                        finalRes.add(new FileInfoEntity(file.getAbsolutePath(), file.getName(), file.isHidden()));
                    } else {
                        finalRes.add(new FileInfoEntity(file));
                    }
                }
            }
        });
        return ResultEntity.success(res);
    }

}
