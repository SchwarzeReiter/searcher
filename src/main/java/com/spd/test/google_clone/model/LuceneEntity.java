package com.spd.test.google_clone.model;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Component
public class LuceneEntity {

    private static String DIR_SUFF="index_base";
    private Path TEMP;
    private Analyzer analyzer;
    private IndexWriterConfig config;

    public LuceneEntity() throws IOException {
        TEMP =  Files.createTempDirectory(DIR_SUFF);
    }



    public Document createDocument(String url, String text) {
        Document document = new Document();
        document.add(new Field(url, text, TextField.TYPE_STORED));
        return document;
    }

    public void indexingPage(String url, String text)  {
        Document document = createDocument(url,text);
        try {
            analyzer = new StandardAnalyzer();
            config = new IndexWriterConfig(analyzer);
            Directory directory = FSDirectory.open(TEMP);
            IndexWriter iWriter = new IndexWriter(directory, config);
            iWriter.addDocument(document);
            iWriter.close();
            directory.close();
        }catch (Exception e)
        {
            e.getStackTrace();
        }
    }


    public Map<String,String>Searching(String find) {
        Map<String,String> result = new LinkedHashMap<>();
        try {
            Directory directory = FSDirectory.open(TEMP);
            Analyzer analyzer = new StandardAnalyzer();
            DirectoryReader ireader = DirectoryReader.open(directory);

            IndexSearcher isearcher = new IndexSearcher(ireader);

            // Parse a simple query that searches for "text":
            List<String> fieldName = new ArrayList<>();

            for (LeafReaderContext subReader : ireader.leaves()) {
                FieldInfos fields = subReader.reader().getFieldInfos();
                for (FieldInfo fieldname : fields) {
                    fieldName.add(fieldname.name);
                    System.out.println(fieldname.name);
                }
            }


               for(String nameField : fieldName) {
                   QueryParser parser = new QueryParser(nameField, analyzer);
                   Query query = parser.parse(find);
                   ScoreDoc[] hits = isearcher.search(query, 1).scoreDocs;


                   if(hits.length>0){
                       System.out.println("Find in "+nameField);
                       String[] data = nameField.split("\n");
                       result.put(data[0],data[1]);
                   }
               }
                ireader.close();
                directory.close();

                for(String key : result.keySet() )
                {
                    System.out.println(key+"  "+result.get(key));
                }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
      }
    }
