package com.ilzf.utils;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
@SuppressWarnings({"unused"})
public class HttpUtil {
    public static HttpStatus getHttpStatus(HttpServletRequest request){
        Integer status  = (Integer) request.getAttribute("javax.servlet.error.status_code");
        return HttpStatus.valueOf(status);
    }
}
