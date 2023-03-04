package com.ilzf.hComic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HCComicCover {
    private String coverUrl;
    private String jumpUrl;
    private String title;
    private String author;
    private List<HCType> types;
    private List<HCTags> tags;
    private String heart;
}
