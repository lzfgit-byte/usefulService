package com.ilzf.hWord.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ImgInfoEntity {

    private String original;
    private String zipUrl;
    private String name;
    private List<DetailEntity> others;


    @Data
    @AllArgsConstructor
    public static class DetailEntity {
        private boolean isCurrent;
        private String coverUrl;
        private String number;
        private String name;
        private String jumpUrl;
    }
}
