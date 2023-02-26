package com.ilzf.image.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageInfoEntity {

    public ImageInfoEntity() {
    }

    public ImageInfoEntity(String url) {
        this.url = url;
    }


    private String name;
    private String url;
    private String path;
    private String base64Data;

}
