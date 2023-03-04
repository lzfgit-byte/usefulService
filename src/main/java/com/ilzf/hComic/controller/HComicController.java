package com.ilzf.hComic.controller;


import com.ilzf.hComic.service.HComicService;
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

}


