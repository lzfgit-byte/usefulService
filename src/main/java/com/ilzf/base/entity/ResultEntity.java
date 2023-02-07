package com.ilzf.base.entity;

import lombok.Data;

@Data
public class ResultEntity<T> {
    private Integer code;
    private T data;
    private String msg;

    public ResultEntity() {
        this.setCode(200);
        this.setMsg("成功");
    }

    public ResultEntity(String msg) {
        this.setCode(200);
        this.setMsg(msg);
    }

    public ResultEntity(T data) {
        this.setCode(200);
        this.setMsg("成功");
        this.setData(data);
    }

    public ResultEntity(Integer code, String msg, T data) {
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

    public static ResultEntity<?> success() {
        return new ResultEntity<>();
    }

    public static ResultEntity<?> success(String msg) {
        return new ResultEntity<>(msg);
    }

    public static ResultEntity<?> success(String msg, boolean isData) {
        return new ResultEntity<>(200, "成功", msg);
    }

    public static <T> ResultEntity<T> success(T data) {
        return new ResultEntity<>(data);
    }

    public static ResultEntity<?> error(String msg) {
        return new ResultEntity<>(500, msg, null);
    }

    public static <T> ResultEntity<T> error(T data) {
        return new ResultEntity<>(500, "错误", data);
    }

    public static ResultEntity<?> error() {
        return new ResultEntity<>(500, "错误", null);
    }
}
