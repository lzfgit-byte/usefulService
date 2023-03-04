package com.ilzf.hComic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HCHomeInfo {
    private List<HCComicCover> serialLatest;
    private List<HCComicCover> latestKoreanComic;
    private List<HCComicCover> recommend;
    private List<HCComicCover> latest;
    public HCHomeInfo(){}
}
