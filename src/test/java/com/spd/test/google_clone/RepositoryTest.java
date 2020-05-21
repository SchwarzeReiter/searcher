package com.spd.test.google_clone;

import com.spd.test.google_clone.errors.RepositoryException;
import com.spd.test.google_clone.model.IndexAndSearch;
import com.spd.test.google_clone.model.IndexAndSearchImpl;
import com.spd.test.google_clone.model.SortType;
import com.spd.test.google_clone.model.WebPage;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
@Component
public class RepositoryTest {

    IndexAndSearch repository;
    Map<String[], String> testData;
    String testQueryOne;
    String testQueryTwo;
    List<WebPage> trueResult;
    List<WebPage> trueResultTwo;
    @Autowired
    Path getRepositoryStoragePath;

    private final Comparator<WebPage> abcComparator = Comparator.comparing(WebPage::getTitle);

    @BeforeEach
    public void prepareData() {
        testQueryOne = "beta alpha";
        testQueryTwo = "gamma test";
        repository = new IndexAndSearchImpl(getRepositoryStoragePath);
        repository.init();
        testData = new HashMap<>();
        trueResult = new ArrayList<>();
        trueResultTwo = new ArrayList<>();

        testData.put(new String[]{"test.com", "page1"}, "test word is alpha");
        testData.put(new String[]{"test2.com", "page2"},  "test beta word is beta");
        testData.put(new String[]{"test3.com", "page3"}, "test word is gamma");
        testData.put(new String[]{"test4.com", "page4"}, "test word is delta");

       trueResult.add(new WebPage(new String[]{"test2.com", "page2"},"test <mark>beta</mark> word is <mark>beta</mark>", 0.1798013f));
        trueResult.add(new WebPage(new String[]{"test.com", "page1"},"test word is <mark>alpha</mark>", 0.13076457f));

        trueResultTwo.add(new WebPage(new String[]{"test3.com", "page3"},"<mark>test</mark> word is <mark>gamma</mark>", 0.26152915f));
        trueResultTwo.add(new WebPage(new String[]{"test.com", "page1"},"<mark>test</mark> word is alpha", 0.13076457f));
        trueResultTwo.add(new WebPage(new String[]{"test2.com", "page2"},"<mark>test</mark> beta word is beta", 0.13076457f));
        trueResultTwo.add(new WebPage(new String[]{"test4.com", "page4"},"<mark>test</mark> word is delta", 0.13076457f));

        for (Map.Entry<String[], String> entry : testData.entrySet()) {
            repository.indexingPage(entry.getKey()[0] + "\n" + entry.getKey()[1], entry.getValue());
        }
    }

    @Test
    public void Search() throws RepositoryException {

        Assert.assertArrayEquals(repository.searchQuery(testQueryOne, SortType.RELEVANT).toArray(), trueResult.toArray());
        trueResult.sort(abcComparator);
        Assert.assertArrayEquals(repository.searchQuery(testQueryOne,SortType.ABC).toArray(), trueResult.toArray());

        Assert.assertArrayEquals(repository.searchQuery(testQueryTwo,SortType.RELEVANT).toArray(), trueResultTwo.toArray());
        trueResultTwo.sort(abcComparator);
        Assert.assertArrayEquals(repository.searchQuery(testQueryTwo,SortType.ABC).toArray(), trueResultTwo.toArray());

    }
}
