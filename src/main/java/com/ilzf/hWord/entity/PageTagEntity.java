package com.ilzf.hWord.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageTagEntity {
    private String tageName;
    private String jumpUrl;
    private String count;
}
