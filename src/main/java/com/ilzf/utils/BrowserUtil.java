package com.ilzf.utils;

import com.teamdev.jxbrowser.chromium.*;

public class BrowserUtil {
    private static final class BrowserInstance {
        static void getBrowser() {
            BrowserContextParams bcp = new BrowserContextParams(BrowserPreferences.getDefaultDataDir());
            bcp.setProxyConfig(new CustomProxyConfig("http=127.0.0.1:10801;https=127.0.0.1:10801;socks=127.0.0.1:10801"));
            BrowserContext browserContext = new BrowserContext(bcp);
            browser = new Browser(browserContext);
        }
        public static Browser browser = null;
    }

    public static Browser getBrowser() {
        return BrowserInstance.browser;
    }
    public static String getHtml(String url){
        Browser browser = getBrowser();
        browser.loadURL(url);
        String html = browser.getHTML();
        return html;
    }
}
