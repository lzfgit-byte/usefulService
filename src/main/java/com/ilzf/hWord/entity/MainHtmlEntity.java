package com.ilzf.hWord.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 获取主页 卡片的详细信息
 * { type: '', coverUrl: '', jumpUrl: '', count: 0 }
 */
@Data
@AllArgsConstructor
public class MainHtmlEntity {
    //类型
    private String type;
    private String coverUrl;
    private String jumpUrl;
    private String width;
    private String height;
    private String count;
    private String title;
}
