package com.spd.test.google_clone.model;


import com.spd.test.google_clone.errors.RepositoryException;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public interface IndexAndSearch {

    void init();
    void indexingPage(String url, String text);
    List<WebPage> searchQuery(String searchQuery,SortType type) throws RepositoryException;
    List<WebPage> sort (List<WebPage> result,SortType type);
}
