package com.ilzf.index.controller;

import com.ilzf.base.entity.ResultEntity;
import com.ilzf.utils.HttpUtil;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ILZFErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResultEntity<?> error(HttpServletRequest request) {
        HttpStatus httpStatus = HttpUtil.getHttpStatus(request);
        return ResultEntity.error(httpStatus.toString());
    }

}
