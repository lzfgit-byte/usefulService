package com.ilzf.base.entity;

import com.ilzf.utils.BrowserUtil;
import com.ilzf.utils.CacheUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

@Data
@AllArgsConstructor
public class BrowserTransEntity {
    private String url;
    private HttpServletResponse response;

    public void writeToResponse() {
        String cacheKey = CacheUtil.getSaveCacheKey(this.getUrl(), null);
        String cacheHeadKey = CacheUtil.getSaveCacheKey(this.getUrl(), "-head");
        BrowserUtil.writeToResponse(cacheKey,cacheHeadKey,this.getResponse());
    }
}
