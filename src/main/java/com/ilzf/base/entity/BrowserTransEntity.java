package com.ilzf.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

@Data
@AllArgsConstructor
public class BrowserTransEntity {
    private String url;
    private HttpServletResponse response;
}
