package com.ilzf.hWord.controller;

import com.ilzf.base.annotation.RequestBodyJson;
import com.ilzf.base.entity.ResultEntity;
import com.ilzf.browser.service.BrowserService;
import com.ilzf.hWord.service.HentaiWordParseService;
import com.ilzf.utils.BrowserUtil;
import com.ilzf.utils.NetUtilILZF;
import com.ilzf.utils.StringUtilIZLF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/hWord")
public class HWordController {

    @Autowired
    HentaiWordParseService service;

    @RequestMapping("/getHtml")
    public ResultEntity<?> getHtml(@RequestBodyJson("path") String path, HttpServletResponse response) {
        if (StringUtilIZLF.isBlankOrEmpty(path)) {
            return ResultEntity.error();
        }
        String html = BrowserUtil.getHtmlCAS(path);
//        String html = NetUtilILZF.getHtmlByUrl(path);
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html为空");
        }
        return ResultEntity.success(html, true);
    }

    @RequestMapping("/getHtmlInfo")
    public ResultEntity<?> getHtmlInfo(@RequestBodyJson("html") String html) {
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error();
        }
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html为空");
        }
        return ResultEntity.success(service.getHtmlInfo(html));
    }

    @RequestMapping("/getVideoInfo")
    public ResultEntity<?> getVideoInfo(@RequestBodyJson("html") String html) {
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error();
        }

        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html为空");
        }
        return ResultEntity.success(service.getVideoInfo(html));
    }

    @RequestMapping("/getImgInfo")
    public ResultEntity<?> getImgInfo(@RequestBodyJson("html") String html) {
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error();
        }

        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html为空");
        }
        return ResultEntity.success(service.getImgInfo(html));
    }

    @RequestMapping("/getImgInfoOnly")
    public ResultEntity<?> getImgInfoOnly(@RequestBodyJson("html") String html) {
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error();
        }

        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html为空");
        }
        return ResultEntity.success(service.getImgInfoOnly(html));
    }

    @RequestMapping("/getImgByte")
    public void getImgByte(@RequestParam("path") String path, HttpServletResponse response) {
//        NetUtilILZF.getByteFromNet(path, response);
//        String path_ = path.substring(0, path.indexOf("?"));
//        new BrowserService(path_).getByte(path_, response);
        BrowserUtil.getByteFromNetCAS(path.substring(0, path.indexOf("?")), response);
    }

    @RequestMapping("/getVideoByte")
    public void getVideoByte(@RequestParam("path") String path, HttpServletResponse response) {
        NetUtilILZF.getByteFromNet(path, response);
    }
}
