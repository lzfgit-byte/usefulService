package com.ilzf.onlineShare.controller;

import cn.hutool.json.JSONArray;
import com.ilzf.base.entity.ResultEntity;
import com.ilzf.onlineShare.entity.OnlineShareEntity;
import com.ilzf.utils.DataUtilILZF;
import com.ilzf.utils.FileUtilILZF;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/onlineShare")
public class OnlineShareController {

    @RequestMapping("/save")
    public ResultEntity<?> saveData(@RequestBody String text) {
        OnlineShareEntity entity = new OnlineShareEntity(text);
        boolean b = DataUtilILZF.saveData(entity);
        return b ? ResultEntity.success() : ResultEntity.error();
    }
    @RequestMapping("/loadData")
    public ResultEntity<?> loadData(){
        JSONArray savedData = DataUtilILZF.getSavedData(OnlineShareEntity.class);
        return ResultEntity.success(savedData);
    }
}
