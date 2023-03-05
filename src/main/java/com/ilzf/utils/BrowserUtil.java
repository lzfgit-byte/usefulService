package com.ilzf.utils;

import cn.hutool.json.JSONObject;
import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.events.*;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.teamdev.jxbrowser.chromium.swing.DefaultNetworkDelegate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class BrowserUtil {
    private static final class BrowserInstance {
        public static Browser browser = null;

        static {
            BrowserContextParams bcp = new BrowserContextParams(BrowserPreferences.getDefaultDataDir());
            bcp.setProxyConfig(new CustomProxyConfig("http=127.0.0.1:10801;https=127.0.0.1:10801;socks=127.0.0.1:10801"));
            BrowserContext browserContext = new BrowserContext(bcp);
            browser = new Browser(browserContext);

//
//            JFrame frame = new JFrame();
//            frame.getContentPane().setEnabled(false);
//            frame.setSize(800, 600);
//            frame.getContentPane().setLayout(null);
//
////            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//            frame.setLocationByPlatform(true);
//            frame.setVisible(true);
//            BrowserView view = new BrowserView(browser);
//            view.setBounds(152, 39, 800, 600);
//            frame.getContentPane().add(view);


            BrowserContext context = browser.getContext();
            context.getNetworkService().setNetworkDelegate(new DefaultNetworkDelegate() {
                @Override
                public void onDataReceived(DataReceivedParams params) {
                    String mimeType = params.getMimeType();
                    String url = params.getURL();
                    if (mimeType.contains("image")) {
                        String cacheKey = CacheUtil.getSaveCacheKey(url, null);
                        String cacheHeadKey = CacheUtil.getSaveCacheKey(url, "-head");
                        byte[] data = params.getData();
                        JSONObject head = new JSONObject();
                        head.set("content-length", data.length);
                        head.set("content-type", mimeType) ;
                        CacheUtil.setCache(cacheKey, data);
                        CacheUtil.setCache(cacheHeadKey,head.toString());
                    }
                    super.onDataReceived(params);
                }
            });

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
                    Browser browser1 = frameLoadEvent.getBrowser();
                    String html = browser1.getHTML();
                    String url = browser1.getURL();
                    String saveCacheKey = CacheUtil.getSaveCacheKey(url, "-html");
                    log.info("onDocumentLoadedInFrame" + html.length());
                    if (StringUtilIZLF.isNotBlankOrEmpty(html) && frameLoadEvent.isMainFrame()) {
//                        CacheUtil.setCache(saveCacheKey, html);
                    }
                }

                @Override
                public void onDocumentLoadedInMainFrame(LoadEvent loadEvent) {
                    log.info("onDocumentLoadedInMainFrame");
                }
            });
            browser.setDownloadHandler(new DownloadHandler() {
                @Override
                public boolean allowDownload(DownloadItem downloadItem) {
                    System.out.println("");
                    return true;
                }
            });
        }
    }

    public static List<String> URL_CACHE = new ArrayList<>();

    public static Browser getBrowser() {
        return BrowserInstance.browser;
    }

    public static void stopBrowser() {
        BrowserInstance.browser.stop();
    }

    public static String getHtml(String url) {
        String saveCacheKey = CacheUtil.getSaveCacheKey(url, "-html");
        if (CacheUtil.hasCache(saveCacheKey)) {
            return CacheUtil.readStrCache(saveCacheKey);
        }
        if (URL_CACHE.contains(url)) {
            return "";
        }
        Browser browser = getBrowser();
        browser.loadURL(url);
        URL_CACHE.add(url);
        return "";
    }

    @SneakyThrows
    public static String getHtmlCAS(String url) {
        String html = getHtml(url);
        if (StringUtilIZLF.isNotBlankOrEmpty(html)) {
            URL_CACHE.clear();
            return html;
        }
        int i = 20;
        int a = 0;
        while (a < i) {
            a++;
            Thread.sleep(100);
            html = getHtml(url);
            if (StringUtilIZLF.isNotBlankOrEmpty(html)) {
                break;
            }
        }
        URL_CACHE.clear();
        return html;
    }

    public static void main(String[] args) {
        //https://cdn-msp.18comic.vip/media/albums/393452_3x4.jpg?v=1677999015
        getHtml("https://cdn-msp.18comic.vip/media/albums/393452_3x4.jpg?v=1677999015");
    }
}
