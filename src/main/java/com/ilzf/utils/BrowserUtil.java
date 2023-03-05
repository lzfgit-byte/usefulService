package com.ilzf.utils;

import cn.hutool.json.JSONUtil;
import com.ilzf.base.entity.ResultEntity;
import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.events.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
public class BrowserUtil {
    private static final class BrowserInstance {
        public static Browser browser = null;

        static {
            BrowserContextParams bcp = new BrowserContextParams(BrowserPreferences.getDefaultDataDir());
            bcp.setProxyConfig(new CustomProxyConfig("http=127.0.0.1:10801;https=127.0.0.1:10801;socks=127.0.0.1:10801"));
            BrowserContext browserContext = new BrowserContext(bcp);
            browser = new Browser(browserContext);
        }
    }

    public static Browser getBrowser() {
        return BrowserInstance.browser;
    }

    public static void stopBrowser() {
        BrowserInstance.browser.stop();
    }

    public static String getHtml(String url, HttpServletResponse response) {

        Browser browser = getBrowser();
        browser.loadURL(url);
        final String[] html = {""};
        browser.addLoadListener(new LoadListener() {
            @Override
            public void onStartLoadingFrame(StartLoadingEvent startLoadingEvent) {
                log.info("onStartLoadingFrame");
            }

            @Override
            public void onProvisionalLoadingFrame(ProvisionalLoadingEvent provisionalLoadingEvent) {
                log.info("onProvisionalLoadingFrame");
            }

            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent finishLoadingEvent) {
                log.info("onFinishLoadingFrame");
            }

            @Override
            public void onFailLoadingFrame(FailLoadingEvent failLoadingEvent) {
                log.info("onFailLoadingFrame");
            }

            @Override
            public void onDocumentLoadedInFrame(FrameLoadEvent frameLoadEvent) {
                log.info("onDocumentLoadedInFrame");
                html[0] = frameLoadEvent.getBrowser().getHTML();
                if(StringUtilIZLF.isNotBlankOrEmpty(html[0])){
                    log.info("frameLoadEvent.getBrowser().getHTML()");
                }
            }

            @Override
            public void onDocumentLoadedInMainFrame(LoadEvent loadEvent) {

            }
        });
        return html[0];
    }
}
