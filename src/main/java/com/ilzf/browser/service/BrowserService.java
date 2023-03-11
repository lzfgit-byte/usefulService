package com.ilzf.browser.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ilzf.browser.impl.LoadListenerImpl;
import com.ilzf.utils.CacheUtil;
import com.ilzf.utils.StringUtilIZLF;
import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.events.FrameLoadEvent;
import com.teamdev.jxbrowser.chromium.swing.DefaultNetworkDelegate;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 为每个请求创建一个jxBrowser
 */
@Data
@Slf4j
public class BrowserService {
    private Browser browser = null;
    public final ThreadLocal<Map<String, byte[]>> bBytes = new ThreadLocal<>();
    private LinkedBlockingQueue<String> lbq = new LinkedBlockingQueue<>();

    public void BrowserService() {
        BrowserContextParams bcp = new BrowserContextParams(BrowserPreferences.getDefaultDataDir());
        bcp.setProxyConfig(new CustomProxyConfig("http=127.0.0.1:10801;https=127.0.0.1:10801;socks=127.0.0.1:10801"));
        BrowserContext browserContext = new BrowserContext(bcp);
        browser = new Browser(browserContext);
        BrowserPreferences preferences = browser.getPreferences();
        preferences.setJavaScriptEnabled(false);

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
            public void onHeadersReceived(HeadersReceivedParams params) {
                String url = params.getURL();
                String cacheHeadKey = CacheUtil.getSaveCacheKey(url, "-head");
                HttpHeadersEx headersEx = params.getHeadersEx();
                Map<String, List<String>> headers = headersEx.getHeaders();
                Set<String> keySet = headers.keySet();
                JSONObject head = new JSONObject();
                keySet.forEach(key -> {
                    List<String> values = headers.get(key);
                    if (values.size() == 1) {
                        head.set(key, values.get(0));
                    }
                });
                CacheUtil.setCache(cacheHeadKey, head.toString());
                super.onHeadersReceived(params);
            }

            @Override
            public void onDataReceived(DataReceivedParams params) {
                String mimeType = params.getMimeType();
                String url = params.getURL();
                if (mimeType.contains("image")) {
                    String cacheKey = CacheUtil.getSaveCacheKey(url, null);
                    byte[] data = params.getData();

                    if (bBytes.get() == null) {
                        HashMap<String, byte[]> map = new HashMap<>();
                        map.put(cacheKey, data);
                        bBytes.set(map);
                    } else {
                        if (bBytes.get().containsKey(cacheKey)) {
                            byte[] bytes = bBytes.get().get(cacheKey);
                            int total = data.length + bytes.length;
                            byte[] newB = new byte[total];
                            System.arraycopy(bytes, 0, newB, 0, bytes.length);
                            System.arraycopy(data, 0, newB, bytes.length, data.length);
                            Map<String, byte[]> map = bBytes.get();
                            map.put(cacheKey, newB);
                            bBytes.set(map);
                        } else {
                            Map<String, byte[]> map = bBytes.get();
                            map.put(cacheKey, params.getData());
                        }

                    }
                    String cacheHeadKey = CacheUtil.getSaveCacheKey(url, "-head");
                    String s = CacheUtil.readStrCache(cacheHeadKey);
                    JSONObject obj = JSONUtil.parseObj(s);
                    Object origSize = obj.get("content-length");
                    int integer = Integer.parseInt(StringUtilIZLF.wrapperString(origSize));
                    if (integer == bBytes.get().get(cacheKey).length) {
                        CacheUtil.setCache(cacheKey, bBytes.get().get(cacheKey));
                        bBytes.set(null);
                        lbq.add("1");
                    }

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
                    lbq.add("1");
                }
            }
        });

    }


    public void stopBrowser() {
        browser.stop();
    }

    @SneakyThrows
    public String getHtml(String url) {
        String saveCacheKey = CacheUtil.getSaveCacheKey(url, "-html");
        if (CacheUtil.hasCache(saveCacheKey)) {
            return CacheUtil.readStrCache(saveCacheKey);
        }
        browser.loadURL(url);
        StopWatch sw = new StopWatch();
        sw.start();
        String take = lbq.take();
        sw.stop();
        log.info("获取:" + "[" + url + "]" + sw.getTotalTimeMillis());
        if (CacheUtil.hasCache(saveCacheKey)) {
            return CacheUtil.readStrCache(saveCacheKey);
        }
        return "";
    }

    public static void writeToResponse(String cacheKey, String cacheHeadKey, HttpServletResponse response) {
        try (OutputStream output = response.getOutputStream()) {
            byte[] bytes = CacheUtil.readByteCache(cacheKey);
            String str = CacheUtil.readStrCache(cacheHeadKey);
            JSONObject object = JSONUtil.parseObj(str);
            Set<String> keySet = object.keySet();
            object.set("Modified", new Date().toString());
            object.set("date", new Date().toString());
            log.info(ArrayUtil.join(keySet.toArray(), ","));
            keySet.forEach(key -> {
                response.setHeader(key, StringUtilIZLF.wrapperString(object.get(key)));
            });

            output.write(bytes);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getByteImageFormatHtml(String imageUrl) {
        Browser browser = getBrowser();
        StringBuffer sb = new StringBuffer("<html><head><meta name=\"viewport\" content=\"width=device-width, minimum-scale=0.1\"><title>425375_3x4.jpg (400×533)</title></head><body style=\"margin: 0px;\"><img style=\"user-select: none; cursor: zoom-in;\" src=\"");
        sb.append(imageUrl);
        sb.append("\" width=\"0\" height=\"0\"></body></html>");
        browser.loadHTML(sb.toString());
    }

    @SneakyThrows
    public void getByte(String url, HttpServletResponse response) {
        String cacheKey = CacheUtil.getSaveCacheKey(url, null);
        String cacheHeadKey = CacheUtil.getSaveCacheKey(url, "-head");
        if (CacheUtil.hasCache(cacheKey)) {
            writeToResponse(cacheKey, cacheHeadKey, response);
            return;
        }
        browser.loadURL(url);
        StopWatch sw = new StopWatch();
        sw.start();
        String take = lbq.take();
        sw.stop();
        log.info("获取:" + "[" + url + "]" + sw.getTotalTimeMillis());
        if (CacheUtil.hasCache(cacheKey)) {
            writeToResponse(cacheKey, cacheHeadKey, response);
        }
    }

}
