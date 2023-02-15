package com.ilzf.onlineShare.controller;

import cn.hutool.json.JSONObject;
import com.ilzf.base.entity.ResultEntity;
import com.ilzf.onlineShare.entity.OnlineShareEntity;
import com.ilzf.utils.DataUtilILZF;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/onlineShare")
public class OnlineShareController {

    @RequestMapping("/save")
    public ResultEntity<?> saveData(@RequestBody String text, HttpServletRequest request) {
        OnlineShareEntity entity = new OnlineShareEntity(text);
        boolean b = DataUtilILZF.saveData(entity);
        return b ? ResultEntity.success() : ResultEntity.error();
    }
    @RequestMapping("/loadData")
    public ResultEntity<?> loadData(){
        JSONObject savedData = DataUtilILZF.getSavedData(OnlineShareEntity.class);
        return savedData == null ? ResultEntity.success("",true) :ResultEntity.success((String) ((JSONObject)savedData.get("a")).get("text"),true);
    }
}
