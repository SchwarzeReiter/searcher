package com.spd.test.google_clone;

import com.spd.test.google_clone.errors.RepositoryError;
import com.spd.test.google_clone.model.Repository;
import com.spd.test.google_clone.model.RepositoryImpl;
import com.spd.test.google_clone.model.WebPage;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

public class RepositoryTest {

    Repository repository;
    Map<String[], String> testData;
    String testQueryOne;
    String testQueryTwo;
    List<WebPage> trueResult;
    List<WebPage> trueResultTwo;

    private final Comparator<WebPage> abcComparator = Comparator.comparing(WebPage::getTitle);

    @BeforeEach
    public void prepareData() throws IOException {
        testQueryOne = "beta alpha";
        testQueryTwo = "gamma test";
        repository = new RepositoryImpl();
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
    public void Search() throws RepositoryError {

        Assert.assertArrayEquals(repository.searchQuery(testQueryOne,0).toArray(), trueResult.toArray());
        trueResult.sort(abcComparator);
        Assert.assertArrayEquals(repository.searchQuery(testQueryOne,1).toArray(), trueResult.toArray());

        Assert.assertArrayEquals(repository.searchQuery(testQueryTwo,0).toArray(), trueResultTwo.toArray());
        trueResultTwo.sort(abcComparator);
        Assert.assertArrayEquals(repository.searchQuery(testQueryTwo,1).toArray(), trueResultTwo.toArray());

    }
}
