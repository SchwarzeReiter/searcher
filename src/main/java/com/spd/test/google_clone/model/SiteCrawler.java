package com.spd.test.google_clone.model;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class SiteCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz|php))$");
    public static LuceneRepository repository;
    public static String userUrl;

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
            String text = htmlParseData.getText();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            System.out.println("Page "+page.getWebURL().getURL());//-----------------------
            System.out.println("Number of outgoing links: " + links.size());//--------------------------
            repository.indexingPage(page.getWebURL().getURL()+"\n"+htmlParseData.getTitle(), text);
            links.stream().forEach(x -> System.out.println(x.getURL()));//--------------------
        }
    }

}
