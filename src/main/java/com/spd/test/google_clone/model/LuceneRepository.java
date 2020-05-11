package com.spd.test.google_clone.model;

import com.spd.test.google_clone.errors.RepositoryError;

import java.util.Comparator;
import java.util.List;

public interface LuceneRepository {

    void init();
    void indexingPage(String url, String text);
    List<WebPage> searchQuery(String searchQuery,int type) throws RepositoryError;
    List<WebPage> sort (List<WebPage> result,Integer type);
    List<WebPage> getResult();
    Comparator<WebPage> getABCComparator();
    Comparator<WebPage> getRelevantComparator();
}
