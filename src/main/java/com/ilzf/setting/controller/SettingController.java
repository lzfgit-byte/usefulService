package com.ilzf.setting.controller;

import com.ilzf.base.entity.ResultEntity;
import com.ilzf.utils.ConfigUtilILZF;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/setting")
public class SettingController {

    @RequestMapping("/listConfig")
    public ResultEntity<?> listConfig() {
        return ResultEntity.success(ConfigUtilILZF.listConfig());
    }

    @RequestMapping("/set")
    public ResultEntity<?> set(@RequestBody Map<String, String> map) {
        ConfigUtilILZF.setMap(map);
        return ResultEntity.success();
    }

    @RequestMapping("/get")
    public ResultEntity<?> get(@RequestParam(value = "key") String key) {
        return ResultEntity.success(ConfigUtilILZF.get(key));
    }
}
