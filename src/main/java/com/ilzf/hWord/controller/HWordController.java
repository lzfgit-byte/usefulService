package com.ilzf.hWord.controller;

import cn.hutool.json.JSONObject;
import com.ilzf.base.entity.ResultEntity;
import com.ilzf.hWord.service.HentaiWordParseService;
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



    @RequestMapping("/getImgByte")
    public void getImgByte(@RequestParam("path") String path, HttpServletResponse response) {
        NetUtilILZF.getImgByte(path, response);
    }
    @RequestMapping("/getVideoByte")
    public void getVideoByte(@RequestParam("path") String path, HttpServletResponse response) {
        NetUtilILZF.getVideoByte(path, response);
    }
}
