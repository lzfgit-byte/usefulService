package com.ilzf.hComic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HCSearchPageInfo {
    private List<HCComicCover> covers;
    private List<HCPageInfo> pageInfos;

    @Data
    @AllArgsConstructor
    public static class HCPageInfo {
        private String jumpUrl;
        private String title;
        private boolean isCurrent;

    }
}

