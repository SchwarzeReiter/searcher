package com.spd.test.google_clone.model;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlerConfiguration  {


    private final String CRAWL_STORAGE_FOLDER ="src/main/resources/data";
    private final SiteCrawlerConfiguration siteCrawlerConfiguration;
    public void crawler(String userUrl) throws Exception {
        siteCrawlerConfiguration.initWebCrawler(userUrl);
        int numberOfCrawlers = 100;
        CrawlConfig config = new CrawlConfig();
        config.setMaxDepthOfCrawling(2);
        config.setCrawlStorageFolder(CRAWL_STORAGE_FOLDER);
        config.setIncludeHttpsPages(true);
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed(userUrl);
        controller.start(SiteCrawler.class, numberOfCrawlers);
    }
}