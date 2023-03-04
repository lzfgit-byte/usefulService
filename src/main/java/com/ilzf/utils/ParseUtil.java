package com.ilzf.utils;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class ParseUtil {
    public static String helpGetElementAttr(Element e, String attr) {
        return e != null ? e.attr(attr) : "";
    }

    public static String helpGetElementText(Element e) {
        return e != null ? e.text() : "";
    }

    public static String helpGetElementAttr(Element e, String... attr) {
        for (String arg : attr) {
            String a = helpGetElementAttr(e, arg);
            if (StringUtilIZLF.isNotBlankOrEmpty(a)){
                return a;
            }
        }
        return "";
    }
}
