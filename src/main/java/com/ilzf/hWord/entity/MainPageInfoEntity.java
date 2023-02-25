package com.ilzf.hWord.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MainPageInfoEntity {
    private Boolean isCurrent;
    private String pageNumber;
    private String jumpUrl;
}
