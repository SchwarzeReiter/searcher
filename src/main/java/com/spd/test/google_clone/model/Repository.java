package com.spd.test.google_clone.model;

import com.spd.test.google_clone.errors.RepositoryError;

import java.io.IOException;
import java.util.List;

public interface Repository {

    void init() throws IOException;
    void indexingPage(String url, String text) ;
    List<WebPage> searchQuery(String searchQuery,int type) throws RepositoryError;
    List<WebPage> sort (List<WebPage> result,Integer type);
}
