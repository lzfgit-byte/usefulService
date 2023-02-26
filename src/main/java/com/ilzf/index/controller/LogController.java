package com.ilzf.index.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/log")
public class LogController {

    @RequestMapping("/log")
    public void log(@RequestBody String map){
        log.info(map.toString());
    }
}
