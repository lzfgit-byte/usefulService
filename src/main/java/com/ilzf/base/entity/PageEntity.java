package com.ilzf.base.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * 分页返参数
 *
 * @param <T>
 */
@Data
public class PageEntity<T> {
    private Integer current;
    private Long total;
    private List<T> data;

    /**
     * 数据 当前页 总数
     *
     * @param data
     * @param current
     * @param total
     */
    public PageEntity(List<T> data, Integer current, Long total) {
        this.setData(data);
        this.setCurrent(current);
        this.setTotal(total);
    }

    public static <K> PageEntity<?> getPageEntity(List<K> data, Integer current, Long total) {
        return new PageEntity<>(data,current,total);
    }
}
