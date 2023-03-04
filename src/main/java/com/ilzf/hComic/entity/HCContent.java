package com.ilzf.hComic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HCContent {
    private String title;
    private String jumpUrl;
    private boolean current;
}
