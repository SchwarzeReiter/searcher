package com.spd.test.google_clone.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class SiteCrawlerConfiguration {

    @Autowired
    public SiteCrawlerConfiguration(LuceneEntity luceneEntity) {
       SiteCrawler.luceneEntity = luceneEntity;
    }

    public void initWebCrawler(String rootUrl)
   {
       SiteCrawler.userUrl = rootUrl ;
   }
}
