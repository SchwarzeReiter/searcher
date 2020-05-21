package com.spd.test.google_clone.model;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class SiteCrawler extends WebCrawler {

    private static final  Pattern FILTERS = Pattern.compile(".*(\\\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz))$");

    private static IndexAndSearch repository;
    private static String userUrl;

    public static void setRepository(IndexAndSearch repository) {
        SiteCrawler.repository = repository;
        repository.init();
    }

    public static void setUserUrl(String userUrl) {
        SiteCrawler.userUrl = userUrl;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
             && href.startsWith(userUrl);

    }

    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = clearText(htmlParseData.getHtml());
            repository.indexingPage(page.getWebURL().getURL()+"\n"+htmlParseData.getTitle(), text);
        }
    }

    public String clearText(String html)
    {
        return Jsoup.parse(html).normalise().text();
    }
}
