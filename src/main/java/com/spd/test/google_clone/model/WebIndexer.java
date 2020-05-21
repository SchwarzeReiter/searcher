package com.spd.test.google_clone.model;

import com.spd.test.google_clone.errors.HttpNotFountException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface WebIndexer {
    void indexTheSite(String url, int depth) throws HttpNotFountException, IOException;
    void checkURL(String url) throws HttpNotFountException ;

}
