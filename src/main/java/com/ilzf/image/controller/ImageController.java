package com.ilzf.image.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.ilzf.base.entity.ResultEntity;
import com.ilzf.image.entity.ImageInfoEntity;
import com.ilzf.utils.FileUtilILZF;
import com.ilzf.utils.NetUtilILZF;
import javafx.collections.transformation.FilteredList;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/image")
@RestController
public class ImageController {

    @RequestMapping("/listNetWOrkQrCode")
    public ResultEntity<List<ImageInfoEntity>> listResultEntity() {
        List<ImageInfoEntity> res = new ArrayList<>();
        List<String> localhost = NetUtilILZF.getLocalhost();
        List<File> qrcodeFiles = new ArrayList<>();
        String tempPath = FileUtilILZF.getTempFilePath();
        final int[] count = {0};
        localhost.forEach(url -> {
            ImageInfoEntity entity = new ImageInfoEntity();
            File file = new File(tempPath + count[0] + ".jpg");
            QrCodeUtil.generate(url, 300, 300, file);
            qrcodeFiles.add(file);
            entity.setPath(tempPath);
            String base64Str = Base64Utils.encodeToString(FileUtil.readBytes(file));
            entity.setBase64Data("data:image/jpeg;base64," + base64Str);
            entity.setName(url);
            count[0]++;
            res.add(entity);
        });

        return ResultEntity.success(res);
    }

    @RequestMapping("/listNetWOrkInfo")
    public ResultEntity<List<ImageInfoEntity>> listNetWOrkInfo() {
        List<ImageInfoEntity> res = new ArrayList<>();
        List<String> localhost = NetUtilILZF.getLocalhost();

        localhost.forEach(str -> {
            res.add(new ImageInfoEntity(str));
        });

        return ResultEntity.success(res);
    }
}
