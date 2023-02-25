package com.ilzf.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
@Slf4j
public class LogUtilILZF {
    public static void log(Object o) {
        String logStr = StringUtilIZLF.wrapperString(o);
        if (StringUtilIZLF.isBlankOrEmpty(logStr)) {
            log("记录空日志");
            return;
        }
        log.info(logStr);
    }
    public static void log(String ...logsStr){
        StringBuffer sb = new StringBuffer();
        Arrays.stream(logsStr).forEach(sb::append);
        log(sb);
    }
}
