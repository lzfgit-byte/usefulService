package com.ilzf.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ilzf.browser.LoadListenerImpl;
import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.events.*;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.teamdev.jxbrowser.chromium.swing.DefaultNetworkDelegate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
public class BrowserUtil {
    private static final class BrowserInstance {
        public static Browser browser = null;
        static {
            BrowserContextParams bcp = new BrowserContextParams(BrowserPreferences.getDefaultDataDir());
            bcp.setProxyConfig(new CustomProxyConfig("http=127.0.0.1:10801;https=127.0.0.1:10801;socks=127.0.0.1:10801"));
            BrowserContext browserContext = new BrowserContext(bcp);
            browser = new Browser(browserContext);
            BrowserPreferences preferences = browser.getPreferences();
            preferences.setJavaScriptEnabled(false);

//
            JFrame frame = new JFrame();
            frame.getContentPane().setEnabled(false);
            frame.setSize(800, 600);
            frame.getContentPane().setLayout(null);

//            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
            BrowserView view = new BrowserView(browser);
            view.setBounds(152, 39, 800, 600);
            frame.getContentPane().add(view);


            BrowserContext context = browser.getContext();
            context.getNetworkService().setNetworkDelegate(new DefaultNetworkDelegate() {
                @Override
                public void onCompleted(RequestCompletedParams params) {
                    System.out.println("");
                    super.onCompleted(params);
                }

                @Override
                public void onDataReceived(DataReceivedParams params) {
                    String mimeType = params.getMimeType();
                    String url = params.getURL();
                    if (mimeType.contains("image")) {
                        String cacheKey = CacheUtil.getSaveCacheKey(url, null);
                        String cacheHeadKey = CacheUtil.getSaveCacheKey(url, "-head");
                        if (CacheUtil.hasCache(cacheKey)) {
                            byte[] bytes = CacheUtil.readByteCache(cacheKey);
                            byte[] data = params.getData();
                            int length = bytes.length + data.length;
                            byte[] newByte = new byte[length];
                            for (int i = 0; i < bytes.length; i++) {
                                newByte[i] = bytes[i];
                            }
                            for (int i = 0; i < data.length; i++) {
                                newByte[bytes.length + i] = bytes[i];
                            }
                            JSONObject head = new JSONObject();
                            head.set("content-length", newByte.length);
                            head.set("content-type", mimeType);
                            head.set("accept-ranges", "bytes");
                            CacheUtil.setCache(cacheKey, newByte);
                            CacheUtil.setCache(cacheHeadKey, head.toString());

                            return;
                        }
                        byte[] data = params.getData();
                        JSONObject head = new JSONObject();
                        head.set("content-length", data.length);
                        head.set("content-type", mimeType);
                        head.set("accept-ranges", "bytes");
                        CacheUtil.setCache(cacheKey, data);
                        CacheUtil.setCache(cacheHeadKey, head.toString());
                    }
                    super.onDataReceived(params);
                }
            });

            browser.addLoadListener(new LoadListenerImpl() {
                @Override
                public void onDocumentLoadedInFrame(FrameLoadEvent frameLoadEvent) {
                    Browser browser1 = frameLoadEvent.getBrowser();
                    String html = browser1.getHTML();
                    String url = browser1.getURL();
                    String saveCacheKey = CacheUtil.getSaveCacheKey(url, "-html");
                    log.info("onDocumentLoadedInFrame" + html.length());
                    if (StringUtilIZLF.isNotBlankOrEmpty(html) && frameLoadEvent.isMainFrame()) {
                        CacheUtil.setCache(saveCacheKey, html);
                    }
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

    public static void getByte(String url) {
        Browser browser = getBrowser();
        browser.loadURL(url);
    }

    public static void getByteFormatHtml(String html) {
        Browser browser = getBrowser();
        StringBuffer sb = new StringBuffer("<html><head><meta name=\"viewport\" content=\"width=device-width, minimum-scale=0.1\"><title>425375_3x4.jpg (400×533)</title></head><body style=\"margin: 0px;\"><img style=\"user-select: none; cursor: zoom-in;\" src=\"");
        sb.append(html);
        sb.append("\" width=\"0\" height=\"0\"></body></html>");
        browser.loadHTML(sb.toString());
    }

    @SneakyThrows
    public static String getHtmlCAS(String url) {
        String html = getHtml(url);
        if (StringUtilIZLF.isNotBlankOrEmpty(html)) {
            URL_CACHE.remove(url);
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
        URL_CACHE.remove(url);
        return html;
    }

    public static void writeToResponse(String cacheKey, String cacheHeadKey, HttpServletResponse response) {
        try (OutputStream output = response.getOutputStream()) {
            byte[] bytes = CacheUtil.readByteCache(cacheKey);
            String str = CacheUtil.readStrCache(cacheHeadKey);
            JSONObject object = JSONUtil.parseObj(str);
            Set<String> keySet = object.keySet();
            object.set("Modified", new Date().toString());
            object.set("date", new Date().toString());
            keySet.forEach(key -> {
                response.setHeader(key, StringUtilIZLF.wrapperString(object.get(key)));
            });

            output.write(bytes);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static void getByteFromNetCAS(String url, HttpServletResponse response) {
        String cacheUrl = url + "image";
        String cacheKey = CacheUtil.getSaveCacheKey(url, null);
        String cacheHeadKey = CacheUtil.getSaveCacheKey(url, "-head");
        if (CacheUtil.hasCache(cacheKey)) {
            writeToResponse(cacheKey, cacheHeadKey, response);
            URL_CACHE.remove(cacheUrl);
            return;
        }
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        getByteFormatHtml(url);
        URL_CACHE.add(cacheUrl);
        int i = 20;
        int a = 0;
        while (a < i) {
            a++;
            Thread.sleep(100);
            if (CacheUtil.hasCache(cacheKey)) {
                writeToResponse(cacheKey, cacheHeadKey, response);
                URL_CACHE.remove(cacheUrl);
                reentrantLock.unlock();
                return;
            }
        }
        reentrantLock.unlock();
        URL_CACHE.remove(cacheUrl);
    }

    public static void main(String[] args) {
        //https://cdn-msp.18comic.vip/media/albums/393452_3x4.jpg?v=1677999015
//        getHtml("https://cdn-msp.18comic.vip/media/albums/296782_3x4.jpg?v=1677941225");
        getByte("https://cdn-msp.18comic.org/media/albums/352011_3x4.jpg?v=1677998986");
    }
}
