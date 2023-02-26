package com.ilzf.hWord.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HtmlInfoEntity {
    private List<MainHtmlEntity> mainHtml;
    private List<MainPageInfoEntity> pageInfo;
    private List<PageTagEntity> tags;
}
