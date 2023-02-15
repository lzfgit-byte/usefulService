package com.ilzf.networkParse.controller;

import com.ilzf.base.entity.ResultEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/network")
@RestController
public class NetworkController {

    @RequestMapping("/getHtmlByUrl")
    public ResultEntity<?> getHtmlByUrl(@RequestParam(value = "url") String url){

        return ResultEntity.success();
    }
}
