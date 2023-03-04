package com.ilzf.index.entity;

import com.ilzf.base.annotation.Table;
import com.ilzf.base.annotation.Unique;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Table("saveSomeData")
public class SaveSomeData {
    @Unique
    private String key;

    private String configStr;
}
