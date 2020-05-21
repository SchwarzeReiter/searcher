package com.spd.test.google_clone.model;

import lombok.Getter;


import java.util.Objects;

@Getter

public class WebPage  {
    private final String url;
    private final String title;
    private final String simpleText;
    private final Float score;

    public WebPage(String[] urlAndTitle,String simpleText, Float score) {
        this.url = urlAndTitle[0];
        this.title = urlAndTitle.length > 1 ? urlAndTitle[1] : "";
        this.score = score;
        this.simpleText = simpleText;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebPage webPage = (WebPage) o;
        return Objects.equals(url, webPage.url) &&
                Objects.equals(title, webPage.title) &&
                Objects.equals(score, webPage.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title, score);
    }


    @Override
    public String toString() {
        return "WebPage{" +
                "URL='" + url + '\'' +
                ", Title='" + title + '\'' +
                ", score=" + score +
                '}';
    }

}
