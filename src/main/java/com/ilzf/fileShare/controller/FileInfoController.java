package com.ilzf.fileShare.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.ilzf.base.entity.ResultEntity;
import com.ilzf.fileShare.entity.FileInfoEntity;
import com.ilzf.fileShare.service.FileShareService;
import com.ilzf.utils.DataUtilILZF;
import com.ilzf.utils.FileUtilILZF;
import com.ilzf.utils.StringUtilIZLF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/file")
public class FileInfoController {

    @Autowired
    FileShareService fileShareService;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @RequestMapping("/uploadFile")
    public ResultEntity<?> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return fileShareService.uploadFile(file);
    }

    /**
     * 下载文件
     * @param IdOrName
     * @param response
     */
    @RequestMapping("/downloadFile")
    public void downloadFile(@RequestParam(value = "id") String IdOrName, HttpServletResponse response) {
        fileShareService.downloadFile(IdOrName,response);
    }
    /**
     * 下载文件
     * @param path
     * @param response
     */
    @RequestMapping("/downloadFileByPath")
    public void downloadFileByPath(@RequestParam(value = "path") String path, HttpServletResponse response) {
        fileShareService.downloadFilebyPath(path,response);
    }

    /**
     * 遍历文件
     * @return
     */
    @RequestMapping("/listFiles")
    public ResultEntity<List<FileInfoEntity>> listFiles(@RequestBody Map<String,String> map) {
       return fileShareService.listFiles(map);
    }
}
