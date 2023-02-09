package com.ilzf.fileShare.entity;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.ilzf.base.annotation.Table;
import com.ilzf.base.annotation.Unique;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 本地存储文件类
 */
@Data
@Table("file")
public class FileInfoEntity {
    public FileInfoEntity(File file){
        this.setName(file.getName());
        this.setPath(file.getAbsolutePath());
        this.setSize(file.length());
    }
    public FileInfoEntity(MultipartFile file,String path){
        this.setName(file.getOriginalFilename());
        this.setPath(path);
        this.setSize(file.getSize());
    }

    //ID
    private String id;
    //文件名字
    private String name;
    //文件路径
    @Unique
    private String path;
    //文件大小
    private Long size;
}
