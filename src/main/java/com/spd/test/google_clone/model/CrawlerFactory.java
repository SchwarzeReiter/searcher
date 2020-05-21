package com.spd.test.google_clone.model;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;


@Component
@RequiredArgsConstructor
public class CrawlerFactory {
    private final IndexAndSearch repository;
    private static final Logger log = LoggerFactory.getLogger(CrawlerFactory.class);
    private final Path getPath;

    public CrawlController configureMyCrawler(String userUrl, int depth) {

        prepareSiteCrawler(userUrl);
        CrawlConfig config = new CrawlConfig();
        config.setMaxDepthOfCrawling(depth);
        config.setCrawlStorageFolder(getPath.toString());
        config.setIncludeHttpsPages(true);
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = null;
        try{
            controller = new CrawlController(config, pageFetcher, robotstxtServer);
            controller.addSeed(userUrl);
        } catch (Exception e) {
            log.error("Can`t config CrawlController");
        }
        return controller;
    }

    private void prepareSiteCrawler(String rootUrl){
        WebURL url = new WebURL();
                url.setURL(rootUrl);

        SiteCrawler.setUserUrl(rootUrl.replace(url.getPath(),"/"));
        SiteCrawler.setRepository(repository);
    }
}