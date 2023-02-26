package com.ilzf.hWord.controller;

import com.ilzf.base.entity.ResultEntity;
import com.ilzf.hWord.entity.VideoInfoEntity;
import com.ilzf.hWord.service.HentaiWordParseService;
import com.ilzf.utils.NetUtilILZF;
import com.ilzf.utils.StringUtilIZLF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/hWord")
public class HWordController {

    @Autowired
    HentaiWordParseService service;

    @RequestMapping("/getHtml")
    public ResultEntity<?> getHtml(@RequestBody Map<String, String> map) {
        String path = map.get("path");
        if (StringUtilIZLF.isBlankOrEmpty(path)) {
            return ResultEntity.error();
        }
        String html = NetUtilILZF.getHtmlByUrl(path);
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html为空");
        }
        return ResultEntity.success(html, true);
    }

    @RequestMapping("/getHtmlInfo")
    public ResultEntity<?> getHtmlInfo(@RequestBody Map<String, String> map) {
        String html = map.get("html");
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error();
        }
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html为空");
        }
        return ResultEntity.success(service.getHtmlInfo(html));
    }

    @RequestMapping("/getVideoInfo")
    public ResultEntity<?> getVideoInfo(@RequestBody Map<String, String> map) {
        String html = map.get("html");
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error();
        }

        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html为空");
        }
        return ResultEntity.success(service.getVideoInfo(html));
    }

    @RequestMapping("/getImgInfo")
    public ResultEntity<?> getImgInfo(@RequestBody Map<String, String> map) {
        String html = map.get("html");
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error();
        }

        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html为空");
        }
        return ResultEntity.success(service.getImgInfo(html));
    }

    @RequestMapping("/getImgInfoOnly")
    public ResultEntity<?> getImgInfoOnly(@RequestBody Map<String, String> map) {
        String html = map.get("html");
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
        NetUtilILZF.getImgByte(path, response);
    }

    @RequestMapping("/getVideoByte")
    public void getVideoByte(@RequestParam("path") String path, HttpServletResponse response) {
        NetUtilILZF.getVideoByte(path, response);
    }
}
