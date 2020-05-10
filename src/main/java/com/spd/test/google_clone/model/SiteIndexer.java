package com.spd.test.google_clone.model;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SiteIndexer implements WebIndexer{

    private final CrawlerConfiguration crawlerConfiguration;

    public void index(String url,int depth) throws Exception {
        depth = depth < 0 ? 3 : depth;
        CrawlController controller =  crawlerConfiguration.crawler(url,depth);
        controller.start(SiteCrawler.class, 10);
    }
}
