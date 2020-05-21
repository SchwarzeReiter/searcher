package com.spd.test.google_clone.model;

import com.spd.test.google_clone.errors.HttpNotFountException;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;


@Component
@RequiredArgsConstructor
public class SiteIndexer implements WebIndexer{

    private final CrawlerFactory crawlerConfiguration;

    public void indexTheSite(String url, int depth) throws HttpNotFountException {
          checkURL(url);
          CrawlController controller = crawlerConfiguration.configureMyCrawler(url,depth);
          controller.start(SiteCrawler.class, 10);
    }

    @Override
    public void checkURL(String url) throws HttpNotFountException {
        if (URLCanonicalizer.getCanonicalURL(url).isEmpty() || !checkConnection(url)) {
                throw new HttpNotFountException();
        }
    }

    private boolean checkConnection(String userUrl)  {
        try {
            final URLConnection conn = new URL(userUrl).openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
