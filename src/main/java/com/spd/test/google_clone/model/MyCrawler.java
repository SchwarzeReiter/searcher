package com.spd.test.google_clone.model;

import com.spd.test.google_clone.GoogleCloneApplication;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.applet.AppletContext;
import java.util.Set;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class MyCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\\\.(css|js|gif|jpg\"\n" +
            "                                                           + \"|png|mp3|mp4|zip|gz|php))$");

    public static LuceneEntity luceneEntity;


    public static String userUrl;


    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "https://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && href.startsWith(userUrl);
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            System.out.println("Page "+page.getWebURL().getURL());
            System.out.println("Number of outgoing links: " + links.size());
            luceneEntity.indexingPage(page.getWebURL().getURL()+"\n"+htmlParseData.getTitle(), text);
            links.stream().forEach(x -> System.out.println(x.getURL()));
        }
    }

}
