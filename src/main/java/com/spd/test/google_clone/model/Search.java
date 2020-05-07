package com.spd.test.google_clone.model;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class Search {

    private  String DIR_INDEX="src/main/java/resources/index_base" ;

    public void Searching(String find) {
       try {
           Path indexPath = Paths.get(DIR_INDEX);
           Directory directory = FSDirectory.open(indexPath);
           Analyzer analyzer = new StandardAnalyzer();
           DirectoryReader ireader = DirectoryReader.open(directory);
           IndexSearcher isearcher = new IndexSearcher(ireader);
           // Parse a simple query that searches for "text":
           QueryParser parser = new QueryParser("fieldname", analyzer);
           Query query = parser.parse(find);
           ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;

           // Iterate through the results:
           for (
                   int i = 0;
                   i < hits.length; i++) {
               Document hitDoc = isearcher.doc(hits[i].doc);

           }
           ireader.close();
           directory.close();
       }catch (Exception e)
       {
           e.printStackTrace();
       }
}
}
