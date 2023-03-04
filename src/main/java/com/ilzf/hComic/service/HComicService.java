package com.ilzf.hComic.service;

import cn.hutool.core.util.ArrayUtil;
import com.ilzf.hComic.entity.*;
import com.ilzf.utils.StringUtilIZLF;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import static com.ilzf.utils.ParseUtil.helpGetElementText;
import static com.ilzf.utils.ParseUtil.helpGetElementAttr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class HComicService {
    private final String BASE_URL = "https://18comic.vip";

    public void loadSingle(List<HCComicCover> res, Element element) {
        String coverUrl = "", jumpUrl = "", title = "", heart = "";
        Element img = element.selectFirst("img");
        Element a = element.selectFirst("a");
        coverUrl = helpGetElementAttr(img, "data-src", "data-original", "src");
        jumpUrl = helpGetElementAttr(a, "href");
        title = helpGetElementText(element.selectFirst(".video-title"));
        List<String> author_ = new ArrayList<>();
        Elements auEls = element.select(".title-truncate.hidden-xs > a");
        auEls.forEach(el -> {
            author_.add(helpGetElementText(el));
        });
        List<HCType> type_ = new ArrayList<>();
        Elements tyEls = element.select(".category-icon > div");
        tyEls.forEach(el -> {
            type_.add(new HCType(helpGetElementText(el)));
        });


        List<HCTags> tags_ = new ArrayList<>();
        Elements tagEls = element.select(".tags > a");
        tagEls.forEach(el -> {
            String title_ = helpGetElementText(el);
            String jumpUrl_ = BASE_URL + helpGetElementAttr(el, "href");
            tags_.add(new HCTags(title_, jumpUrl_));
        });
        heart = helpGetElementText(element.selectFirst(".label-loveicon span"));
        res.add(new HCComicCover(coverUrl, jumpUrl, title, ArrayUtil.join(author_, ","), type_, tags_, heart));
    }

    public void loadRes(List<HCComicCover> res, Element element) {
        element.children().forEach(el -> {
            loadSingle(res, el);
        });
    }

    //连载最新
    public List<HCComicCover> getSerialLatest(Document doc) {
        Elements selects = doc.select(".owl-carousel.owl-comic-block");
        List<HCComicCover> res = new ArrayList<>();
        if (selects.size() >= 1) {
            loadRes(res, selects.get(0));
        }
        return res;
    }

    //最新韩漫
    public List<HCComicCover> getKoreanComic(Document doc) {
        Elements selects = doc.select(".owl-carousel.owl-comic-block");
        List<HCComicCover> res = new ArrayList<>();
        if (selects.size() >= 2) {
            loadRes(res, selects.get(1));
        }
        return res;
    }

    //本子推荐
    public List<HCComicCover> getRecommend(Document doc) {
        Elements selects = doc.select(".owl-carousel.owl-comic-block");
        List<HCComicCover> res = new ArrayList<>();
        if (selects.size() >= 3) {
            loadRes(res, selects.get(2));
        }
        return res;
    }

    //最新本子
    public List<HCComicCover> getLatest(Document doc) {
        List<HCComicCover> res = new ArrayList<>();
        Elements select = doc.select(".col-sm-12");
        if (select.size() >= 2) {
            Element element = select.get(1);
            Element element1 = element.selectFirst(".col-xs-6.col-sm-4.col-md-3.col-lg-3.list-col.col-xl-2");
            loadRes(res, element1);
        }
        return res;
    }

    /**
     * 获取主页信息
     *
     * @param html
     * @return
     */
    public HCHomeInfo getHomeInfo(String html) {
        Document doc = Jsoup.parse(html);
        return new HCHomeInfo(getSerialLatest(doc), getKoreanComic(doc), getRecommend(doc), getLatest(doc));
    }


    public List<HCContent> getContents(Document doc) {
        List<HCContent> res = new ArrayList<>();
        Element contentConter = doc.selectFirst(".btn-toolbar");
        Elements aS = contentConter.select("a");
        aS.forEach(el -> {
            String jumpUrl = BASE_URL + helpGetElementAttr(el, "href");
            String li = helpGetElementText(el.selectFirst("li"));
            String[] split = li.split("\n");
            String title = split.length >= 2 ? split[1] : "";
            res.add(new HCContent(title, jumpUrl, false));
        });
        return res;
    }

    public HCDetail getComicDetailInfo(String html) {
        Document doc = Jsoup.parse(html);
        String des = helpGetElementText(doc.selectFirst("#intro-block > div:first-child"));
        String reading = BASE_URL + helpGetElementAttr(doc.selectFirst("a.reading"), "href");
        String title = helpGetElementText(doc.selectFirst("title"));
        List<HCContent> contents = getContents(doc);
        return new HCDetail(des, reading, title, contents);
    }

    public HCReadImgInfo getReaderInfos(String html) {
        Document doc = Jsoup.parse(html);
        List<HCReadImgInfo.HCCReadImg> imgs = new ArrayList<>();
        Elements pages = doc.select(".scramble-page");
        pages.forEach(element -> {
            imgs.add(new HCReadImgInfo.HCCReadImg(helpGetElementAttr(element.selectFirst("img"), "data-original")));
        });
        String html_sub = html.substring(html.indexOf("lang_delete_photo_ask"), html.indexOf("page_initial"));
        String[] infos = html_sub.split(";");
        String scramble_id$ = "", aid$ = "";
        if (infos.length >= 2) {
            String info = infos[1];
            String[] split = info.split("=");
            if (split.length >= 2) {
                scramble_id$ = split[1].trim();
            }
        }
        if (infos.length >= 4) {
            String info = infos[3];
            String[] split = info.split("=");
            if (split.length >= 2) {
                aid$ = split[1].trim();
            }
        }
        return new HCReadImgInfo(aid$, imgs, scramble_id$);
    }

    public HCSearchPageInfo getSearchInfo(String html) {
        Document doc = Jsoup.parse(html);
        List<HCComicCover> res = new ArrayList<>();
        Elements rows = doc.select(".row.m-0 > div.col-xs-6.col-sm-6.col-md-4.col-lg-3.list-col");
        rows.forEach(el -> {
            loadSingle(res, el);
        });
        rows = doc.select(".row > div.col-xs-6.col-sm-4.col-md-3.col-lg-3.list-col.col-xl-2");
        rows.forEach(el -> {
            loadSingle(res, el);
        });

        return new HCSearchPageInfo(res, getPageInfo(doc));
    }

    public List<HCSearchPageInfo.HCPageInfo> getPageInfo(Document doc) {
        List<HCSearchPageInfo.HCPageInfo> res = new ArrayList<>();
        Elements liEls = doc.select(".hidden-xs .pagination > li");
        liEls.forEach(el -> {
            boolean isCurrent = el.hasClass("active");
            String jumpUrl = helpGetElementAttr(el.selectFirst("a"), "href");
            String title = StringUtilIZLF.getNotNullStrFormArr(helpGetElementText(doc.selectFirst("a")), helpGetElementText(doc.selectFirst("span")));
            if (StringUtilIZLF.isNotBlankOrEmpty(title)) {
                res.add(new HCSearchPageInfo.HCPageInfo(jumpUrl, title, isCurrent));
            }
        });
        return res;
    }
}





















