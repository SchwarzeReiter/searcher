package com.spd.test.google_clone.model;



import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Indexer {

    private static String DIR_INDEX="src/main/java/resources/index_base" ;

    public Document createDocument(String url, String text) {
        Document document = new Document();
        document.add(new Field(url, text, TextField.TYPE_STORED));
        return document;
    }

    public void indexingPage(String url, String text)  {
        Document document = createDocument(url,text);
        Analyzer analyzer = new StandardAnalyzer();
        try {

            Directory directory = FSDirectory.open(Paths.get(DIR_INDEX));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter iWriter = new IndexWriter(directory, config);
            iWriter.addDocument(document);
            iWriter.close();
            directory.close();
        }catch (Exception e)
        {
            e.getStackTrace();
        }
    }




}
