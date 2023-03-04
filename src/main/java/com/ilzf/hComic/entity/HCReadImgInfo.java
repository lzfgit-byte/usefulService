package com.ilzf.hComic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HCReadImgInfo {
    private String aid;
    private List<HCCReadImg> imgs;
    private String scramble_id;

    @Data
    @AllArgsConstructor
    public static class HCCReadImg {
        private String src;
    }
}
