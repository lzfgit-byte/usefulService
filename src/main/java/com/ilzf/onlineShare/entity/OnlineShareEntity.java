package com.ilzf.onlineShare.entity;

import com.ilzf.base.annotation.Table;
import com.ilzf.base.annotation.Unique;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Table("onlineShare")
@AllArgsConstructor
public class OnlineShareEntity {
    @Unique
    String text;
}
