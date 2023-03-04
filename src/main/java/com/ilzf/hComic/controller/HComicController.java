package com.ilzf.hComic.controller;


import com.ilzf.base.annotation.RequestBodyJson;
import com.ilzf.base.entity.ResultEntity;
import com.ilzf.hComic.service.HComicService;
import com.ilzf.utils.StringUtilIZLF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/hcomic")
@Slf4j
public class HComicController {

    @Resource
    HComicService hComicService;

    @RequestMapping("/getHomeInfo")
    public ResultEntity<?> getHomeInfo(@RequestBodyJson("html") String html) {
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html 为空");
        }
        return ResultEntity.success(hComicService.getHomeInfo(html));
    }

    @RequestMapping("/getComicDetailInfo")
    public ResultEntity<?> getComicDetailInfo(@RequestBodyJson("html") String html) {
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html 为空");
        }
        return ResultEntity.success(hComicService.getComicDetailInfo(html));
    }

    @RequestMapping("/getReaderInfos")
    public ResultEntity<?> getReaderInfos(@RequestBodyJson("html") String html) {
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html 为空");
        }
        return ResultEntity.success(hComicService.getReaderInfos(html));
    }
    @RequestMapping("/getSearchInfo")
    public ResultEntity<?> getSearchInfo(@RequestBodyJson("html") String html) {
        if (StringUtilIZLF.isBlankOrEmpty(html)) {
            return ResultEntity.error("html 为空");
        }
        return ResultEntity.success(hComicService.getSearchInfo(html));
    }

}


