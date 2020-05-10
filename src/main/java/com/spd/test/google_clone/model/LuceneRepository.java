package com.spd.test.google_clone.model;

import java.util.List;

public interface LuceneRepository {

    void init();
    void indexingPage(String url, String text);
    List<WebPage> searchQuery(String searchQuery);

}
