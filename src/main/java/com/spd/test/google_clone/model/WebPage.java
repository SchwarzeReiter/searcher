package com.spd.test.google_clone.model;

import lombok.Getter;


import java.util.Objects;

@Getter

public class WebPage  {
    private final String URL;
    private final String Title;
    private final String simpleText;
    private final Float score;



    public WebPage(String[] urlAndTitle,String simpleText, Float score) {
        this.URL = urlAndTitle[0];
        this.Title = urlAndTitle.length > 1 ? urlAndTitle[1] : "";
        this.score = score;
        this.simpleText = simpleText;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebPage webPage = (WebPage) o;
        return Objects.equals(URL, webPage.URL) &&
                Objects.equals(Title, webPage.Title) &&
                Objects.equals(score, webPage.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(URL, Title, score);
    }


    @Override
    public String toString() {
        return "WebPage{" +
                "URL='" + URL + '\'' +
                ", Title='" + Title + '\'' +
                ", score=" + score +
                '}';
    }

}
