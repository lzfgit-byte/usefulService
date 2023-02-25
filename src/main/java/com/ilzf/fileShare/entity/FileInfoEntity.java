package com.ilzf.fileShare.entity;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.ilzf.base.annotation.Table;
import com.ilzf.base.annotation.Unique;
import com.ilzf.utils.FileUtilILZF;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 本地存储文件类
 */
@Data
@Table("file")
public class FileInfoEntity {
    public FileInfoEntity(File file) {
        String absPath = file.getAbsolutePath();
        this.setName(file.getName());
        this.setPath(absPath);
        this.setSize(file.length());
        if(!file.isDirectory()){
            int a = absPath.lastIndexOf(".");
            int b = absPath.lastIndexOf(FileUtilILZF.SEPARATOR);
            if(a > b){
                this.setFileType(absPath.substring(absPath.lastIndexOf(".")));
                this.setOrder(2);
            }else {
                this.setFileType("文件");
                this.setOrder(1);
            }

        }
        this.setHidden(file.isHidden());
        if(file.isHidden()) this.setOrder(3);
    }

    public FileInfoEntity(String path,String name,boolean isHidden) {
        this.path = path;
        this.name = name;
        this.setHidden(isHidden);
        if(isHidden){
            this.setOrder(3);
        }else {
            this.setOrder(1);
        }

    }

    public FileInfoEntity(MultipartFile file, String path) {
        this.setName(file.getOriginalFilename());
        this.setPath(path);
        this.setSize(file.getSize());
        if(path.lastIndexOf(".") > -1){
            this.setFileType(path.substring(path.lastIndexOf(".")));
            this.setOrder(2);
        }

    }

    //文件名字
    private String name;
    //文件路径
    @Unique
    private String path;
    //文件大小
    private Long size;
    //文件类型
    private String fileType;
    //是否隐藏
    private boolean hidden;
    //文件类型
    private Integer order;
}
