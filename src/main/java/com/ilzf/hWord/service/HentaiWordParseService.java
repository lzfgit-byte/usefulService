package com.ilzf.hWord.service;

import com.ilzf.base.entity.ResultEntity;
import com.ilzf.hWord.entity.MainPageCardInfoEntity;
import com.ilzf.hWord.entity.MainPageInfoEntity;
import com.ilzf.utils.NetUtilILZF;
import com.ilzf.utils.StringUtilIZLF;
import lombok.SneakyThrows;
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
    private static final String MAIN_URL = "https://thehentaiworld.com/?new";

    public String helpGetElementAttr(Element e, String attr) {
        return e != null ? e.attr(attr) : "";
    }

    public String helpGetElementText(Element e) {
        return e != null ? e.text() : "";
    }

    public ResultEntity<?> getMainHtml(String html) {
        Document doc = Jsoup.parse(html);
        List<MainPageCardInfoEntity> res = new ArrayList<>();
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
            jumpUrl = helpGetElementAttr(thumb.selectFirst("a"), "src");
            type = helpGetElementText(thumb.selectFirst("h4"));
            count = helpGetElementText(thumb.selectFirst("span"));
            MainPageCardInfoEntity foo = new MainPageCardInfoEntity(type, coverUrl, jumpUrl, width, height, count, title);
            res.add(foo);
        });
        return ResultEntity.success(res);
    }

    public ResultEntity<?> getPageInfo(String html) {
        List<MainPageInfoEntity> res = new ArrayList<>();
        Document doc = Jsoup.parse(html);
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

        return ResultEntity.success(res);
    }

    public static void main(String[] args) {
        String htmlByUrl = NetUtilILZF.getHtmlByUrl(MAIN_URL);
        System.out.println(htmlByUrl);
        if(StringUtilIZLF.isBlankOrEmpty(htmlByUrl)){
            System.out.println("c");
            return;
        }
        ResultEntity<?> resultEntity = new HentaiWordParseService().getMainHtml(htmlByUrl);
        ResultEntity<?> resultEntity2 = new HentaiWordParseService().getPageInfo(htmlByUrl);
        System.out.println("");
    }
}
