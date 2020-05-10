package com.spd.test.google_clone.model;

import org.springframework.stereotype.Component;

@Component
public interface WebIndexer {
    void index(String url, int depth) throws Exception;
}
