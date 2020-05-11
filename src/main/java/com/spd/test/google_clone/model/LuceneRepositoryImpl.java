package com.spd.test.google_clone.model;

import com.spd.test.google_clone.errors.RepositoryError;
import com.spd.test.google_clone.errors.RepositoryErrorsEnum;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


@Component
public class LuceneRepositoryImpl implements LuceneRepository {

    private static String DIR_SUFF="index_base";
    private Path TEMP;
    private Analyzer analyzer;
    private IndexWriterConfig config;
    public  List<WebPage> result;

    public void init()  {
        try {
            TEMP =  Files.createTempDirectory(DIR_SUFF);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public List<WebPage> getResult()
    {
        return result;
    }

    @Override
    public Comparator<WebPage> getABCComparator() {
        return abcComparator;
    }

    @Override
    public Comparator<WebPage> getRelevantComparator() {
        return relevantComparator;
    }

    private Document createDocument(String url, String text) {
        Document document = new Document();
        document.add(new Field(url, text, TextField.TYPE_STORED));
        return document;
    }

    public void indexingPage(String urlAndTitle, String text)  {
        Document document = createDocument(urlAndTitle,text);
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



    private final Comparator<WebPage> abcComparator = Comparator.comparing(WebPage::getTitle);

    private final  Comparator<WebPage> relevantComparator = (webPageOne, webPageTwo) -> {
        if(webPageOne.getScore() < webPageTwo.getScore()){return 1;}
        if(webPageOne.getScore() > webPageTwo.getScore()){return -1;}
        return webPageOne.getTitle().compareTo(webPageTwo.getTitle());
    };




    public List<WebPage> searchQuery(String searchQuery,int type) throws RepositoryError {

        result = new ArrayList<>();
        if(TEMP == null) {
                throw new RepositoryError(RepositoryErrorsEnum.REPOSITORY_IS_EMPTY);
            }
           try{
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
                   Query query = parser.parse(searchQuery.toLowerCase());
                   ScoreDoc[] hits = isearcher.search(query, 1, Sort.RELEVANCE,true).scoreDocs;

                   if(hits.length>0){
                       System.out.println("Find in "+nameField);
                       result.add(new WebPage(nameField.split("\n"),hits[0].score));
                   }
               }
                ireader.close();
                directory.close();

           result.sort(relevantComparator);
            result.forEach(System.out::println);


           // result.sort(abcComparator);
            System.out.println("---------");

           result.forEach(System.out::println);

        }
           catch (org.apache.lucene.index.IndexNotFoundException e)
           {
               e.printStackTrace();
               throw new RepositoryError(RepositoryErrorsEnum.REPOSITORY_IS_EMPTY);

           }
           catch (Exception e) {
            e.printStackTrace();
        }

        return sort(result,type);
      }

    @Override
    public List<WebPage> sort(List<WebPage> result, Integer type) {
        if(type == 0)
        {
           result.sort(relevantComparator);
           return result;
        }
        result.sort(abcComparator);
        return result;
    }
}
