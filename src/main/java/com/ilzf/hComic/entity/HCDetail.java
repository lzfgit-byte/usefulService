package com.ilzf.hComic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HCDetail {
    public HCDetail(){};
    private String des;
    private String reading;
    private String title;
    private List<HCContent> contents;
}
