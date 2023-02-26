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

    @RequestMapping("/getMainHtml")
    public ResultEntity<?> getMainHtml() {
        String htmlByUrl = NetUtilILZF.getHtmlByUrl(HentaiWordParseService.MAIN_URL);
        System.out.println(htmlByUrl);
        if (StringUtilIZLF.isBlankOrEmpty(htmlByUrl)) {
            return ResultEntity.error("网络错误");
        }
        ResultEntity<?> mainHtml = new HentaiWordParseService().getMainHtml(htmlByUrl);
        ResultEntity<?> pageInfo = new HentaiWordParseService().getPageInfo(htmlByUrl);
        JSONObject json = new JSONObject();
        json.putOnce("mainHtml", mainHtml);
        json.putOnce("pageInfo", pageInfo);
        return ResultEntity.success(json);
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
