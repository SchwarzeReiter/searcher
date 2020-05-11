package com.spd.test.google_clone;

import com.spd.test.google_clone.errors.RepositoryError;
import com.spd.test.google_clone.model.LuceneRepository;
import com.spd.test.google_clone.model.LuceneRepositoryImpl;
import com.spd.test.google_clone.model.WebPage;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class RepositoryTest {

    LuceneRepository repository;
    Map<WebPage, String> testData;
    String testQueryOne;
    String testQueryTwo;
    List<WebPage> trueResult;
    List<WebPage> trueResultTwo;

    private final Comparator<WebPage> abcComparator = Comparator.comparing(WebPage::getTitle);

    @BeforeEach
    public void prepareData() {
        testQueryOne = "beta alpha";
        testQueryTwo = "gamma test";
        repository = new LuceneRepositoryImpl();
        repository.init();
        testData = new HashMap<>();
        trueResult = new ArrayList<>();
        trueResultTwo = new ArrayList<>();

        testData.put(new WebPage(new String[]{"test.com", "page1"}, 0.1f), "test word is alpha");
        testData.put(new WebPage(new String[]{"test2.com", "page2"}, 0.2f), "test beta word is beta");
        testData.put(new WebPage(new String[]{"test3.com", "page3"}, 0.3f), "test word is gamma");
        testData.put(new WebPage(new String[]{"test4.com", "page4"}, 0.4f), "test word is delta");

        trueResult.add(new WebPage(new String[]{"test2.com", "page2"}, 0.1798013f));
        trueResult.add(new WebPage(new String[]{"test.com", "page1"}, 0.13076457f));

        trueResultTwo.add(new WebPage(new String[]{"test3.com", "page3"}, 0.26152915f));
        trueResultTwo.add(new WebPage(new String[]{"test.com", "page1"}, 0.13076457f));
        trueResultTwo.add(new WebPage(new String[]{"test2.com", "page2"}, 0.13076457f));
        trueResultTwo.add(new WebPage(new String[]{"test4.com", "page4"}, 0.13076457f));

        for (Map.Entry<WebPage, String> entry : testData.entrySet()) {
            repository.indexingPage(entry.getKey().getURL() + "\n" + entry.getKey().getTitle(), entry.getValue());
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
