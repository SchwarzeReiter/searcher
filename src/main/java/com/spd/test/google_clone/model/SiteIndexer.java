package com.spd.test.google_clone.model;

import com.spd.test.google_clone.errors.HttpNotFountError;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;


@Component
@RequiredArgsConstructor
public class SiteIndexer implements WebIndexer {

    private final CrawlerConfiguration crawlerConfiguration;

    public void indexTheSite(String url, int depth) throws Exception {
        depth = depth < 0 ? 3 : depth;
        checkURL(url);
        CrawlController controller = crawlerConfiguration.configureMyCrawler(url, depth);
        ;
        controller.startNonBlocking(SiteCrawler.class, 10);
    }

    @Override
    public void checkURL(String url) throws Exception {
        if (URLCanonicalizer.getCanonicalURL(url).isEmpty() | !checkConnection(url)) {
            throw new HttpNotFountError(404);
        }
    }

    private static boolean checkConnection(String userUrl) {
        try {
            final URLConnection conn = new URL(userUrl).openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }
}
