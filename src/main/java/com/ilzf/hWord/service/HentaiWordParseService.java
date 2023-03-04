package com.ilzf.hWord.service;

import com.ilzf.hWord.entity.*;
import com.ilzf.utils.NetUtilILZF;
import com.ilzf.utils.ParseUtil;
import com.ilzf.utils.StringUtilIZLF;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HentaiWordParseService {
    public static final String MAIN_URL = "https://thehentaiworld.com/?new";

    public String helpGetElementAttr(Element e, String attr) {
        return ParseUtil.helpGetElementAttr(e,attr);
    }

    public String helpGetElementText(Element e) {
        return ParseUtil.helpGetElementText(e);
    }

    //主页信息
    public List<MainHtmlEntity> getMainHtml(Document doc) {
        List<MainHtmlEntity> res = new ArrayList<>();
        Elements thumbs = doc.select("#thumbContainer .thumb");
        thumbs.forEach(thumb -> {
            Element element = thumb.selectFirst("[itemprop=\"thumbnail\"]");
            String type = "", coverUrl = "", jumpUrl = "", width = "", height = "", count = "", title = "";
            if (element != null) {
                coverUrl = element.attr("src");
                width = element.attr("width");
                height = element.attr("height");
                title = element.attr("alt");
            }
            jumpUrl = helpGetElementAttr(thumb.selectFirst("a"), "href");
            type = helpGetElementText(thumb.selectFirst("h4"));
            count = helpGetElementText(thumb.selectFirst("span"));
            MainHtmlEntity foo = new MainHtmlEntity(type, coverUrl, jumpUrl, width, height, count, title);
            res.add(foo);
        });
        return res;
    }

    //分页信息
    public List<MainPageInfoEntity> getPageInfo(Document doc) {
        List<MainPageInfoEntity> res = new ArrayList<>();
        doc.select("#more-hentai li").forEach(elem -> {
            boolean isCurrent = false;
            String pageNumber = "", jumpUrl = "";
            Element page = null, prev = null, gap = null, next = null, current = null;
            page = elem.selectFirst(".page");
            prev = elem.selectFirst(".prev");
            gap = elem.selectFirst(".gap");
            next = elem.selectFirst(".next");
            current = elem.selectFirst(".current");
            if (StringUtilIZLF.isNotBlankOrEmpty(helpGetElementAttr(prev, "href"))) {
//                isCurrent = false;
                pageNumber = helpGetElementText(prev);
                jumpUrl = helpGetElementAttr(prev, "href");
            } else if (StringUtilIZLF.isNotBlankOrEmpty(helpGetElementText(current))) {
                isCurrent = true;
                pageNumber = helpGetElementText(current);
                jumpUrl = "";
            } else if (StringUtilIZLF.isNotBlankOrEmpty(helpGetElementAttr(page, "href"))) {
//                isCurrent = false;
                pageNumber = helpGetElementText(page);
                jumpUrl = helpGetElementAttr(page, "href");
            } else if (StringUtilIZLF.isNotBlankOrEmpty(helpGetElementText(gap))) {
//                isCurrent = false;
                pageNumber = helpGetElementText(gap);
                jumpUrl = "";
            } else if (StringUtilIZLF.isNotBlankOrEmpty(helpGetElementAttr(next, "href"))) {
//                isCurrent = false;
                pageNumber = helpGetElementText(next);
                jumpUrl = helpGetElementAttr(next, "href");
            }
            MainPageInfoEntity foo = new MainPageInfoEntity(isCurrent, pageNumber, jumpUrl);
            res.add(foo);
        });

        return res;
    }

    //标签信息
    public List<PageTagEntity> getPageTags(Document doc) {
        List<PageTagEntity> res = new ArrayList<>();
        Elements elements = doc.select("#tags li");
        elements.forEach(element -> {
            String jumpUrl = "", tageName = "", count = "";
            jumpUrl = helpGetElementAttr(element.selectFirst("a"), "href");
            tageName = helpGetElementText(element.selectFirst("a"));
            count = helpGetElementText(element.selectFirst("span"));
            PageTagEntity entity = new PageTagEntity(tageName, jumpUrl, count);
            res.add(entity);
        });
        return res;
    }

    public HtmlInfoEntity getHtmlInfo(String html) {
        Document doc = Jsoup.parse(html);
        return new HtmlInfoEntity(getMainHtml(doc), getPageInfo(doc), getPageTags(doc));
    }

    //获取详细的视频信息
    public VideoInfoEntity getVideoInfo(String html) {
        Document doc = Jsoup.parse(html);
        String videoSrc = "", tite = "";
        videoSrc = helpGetElementAttr(doc.selectFirst("#video source"), "src");
        tite = helpGetElementText(doc.selectFirst("#grid > h1"));
        return new VideoInfoEntity(videoSrc, tite);
    }

    //获取脱线详细信息
    public ImgInfoEntity getImgInfo(String html) {
        Document doc = Jsoup.parse(html);
        String original = "", zipUrl = "", name = "";
        List<ImgInfoEntity.DetailEntity> others = new ArrayList<>();
        original = helpGetElementAttr(doc.selectFirst("#info > li:nth-child(3) > a"), "href");
        zipUrl = helpGetElementAttr(doc.selectFirst(".doujin-page"), "src");
        name = helpGetElementAttr(doc.selectFirst(".doujin-page"), "alt");

        //其他图片
        Elements elements = doc.select("#miniThumbContainer .minithumb");
        elements.forEach(ele -> {
            boolean isCurrent = false;
            String coverUrl = "", name_ = "", number = "", jumpUrl = "";
            isCurrent = ele.hasClass("current");
            coverUrl = helpGetElementAttr(ele.selectFirst("img"), "src");
            name_ = helpGetElementAttr(ele.selectFirst("img"), "alt");
            number = helpGetElementText(ele.selectFirst("h4"));
            jumpUrl = helpGetElementAttr(ele.selectFirst("a"), "href");
            others.add(new ImgInfoEntity.DetailEntity(isCurrent, coverUrl, number, name_, jumpUrl));
        });
        return new ImgInfoEntity(original, zipUrl, name, others);
    }

    //单纯的imgInfo
    public ImgInfoEntity getImgInfoOnly(String html) {
        Document doc = Jsoup.parse(html);
        String original = "", zipUrl = "", name = "";
        List<ImgInfoEntity.DetailEntity> others = new ArrayList<>();
        original = helpGetElementAttr(doc.selectFirst("#info > li:nth-child(3) > a"), "href");
        zipUrl = helpGetElementAttr(doc.selectFirst(".doujin-page"), "src");
        name = helpGetElementAttr(doc.selectFirst(".doujin-page"), "alt");

        return new ImgInfoEntity(original, zipUrl, name, others);
    }

    public static void main(String[] args) {
        String htmlByUrl = NetUtilILZF.getHtmlByUrl(MAIN_URL);
        System.out.println(htmlByUrl);
        if (StringUtilIZLF.isBlankOrEmpty(htmlByUrl)) {
            System.out.println("c");
            return;
        }
        HtmlInfoEntity htmlInfo = new HentaiWordParseService().getHtmlInfo(htmlByUrl);
        System.out.println("");
    }
}
