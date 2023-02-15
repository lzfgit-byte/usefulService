package com.ilzf.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;

import java.nio.charset.StandardCharsets;

public class LogUtilILZF {
    public static final String LOG_FILE = "log.txt";

    public static void log(Object o) {
        String logPath = FileUtilILZF.getSaveDataPath() + "log.txt";
        String logStr = StringUtilIZLF.wrapperString(o);
        if (StringUtilIZLF.isBlankOrEmpty(logStr)) {
            log("记录空日志");
            return;
        }
        FileUtil.appendString("[" + DateUtil.now() + "]  " + logStr + StringUtilIZLF.LINE_BREAK, logPath, StandardCharsets.UTF_8);
    }
}
