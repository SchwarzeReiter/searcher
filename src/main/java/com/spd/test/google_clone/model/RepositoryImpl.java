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
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.TokenSources;


import org.springframework.stereotype.Component;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


@Component
public class RepositoryImpl implements Repository {

    private static String DIR_SUFF = "index_base";
    private Path TEMP;
    private Analyzer analyzer;
    private IndexWriterConfig config;
    public List<WebPage> result;

    public void init() throws IOException {

        TEMP = Files.createTempDirectory(DIR_SUFF);

    }

    private Document createDocument(String url, String text) {
        Document document = new Document();
        document.add(new Field(url, text, TextField.TYPE_STORED));
        return document;
    }

    public void indexingPage(String urlAndTitle, String text) {
       try {
           Document document = createDocument(urlAndTitle, text);
        analyzer = new StandardAnalyzer();
        config = new IndexWriterConfig(analyzer);
        Directory directory = FSDirectory.open(TEMP);
        IndexWriter iWriter = new IndexWriter(directory, config);
        iWriter.addDocument(document);
        iWriter.close();
        directory.close();}
       catch (Exception e){
           e.printStackTrace();
       }

    }


    private final Comparator<WebPage> abcComparator = Comparator.comparing(WebPage::getTitle);

    private final Comparator<WebPage> relevantComparator = (webPageOne, webPageTwo) -> {
        if (webPageOne.getScore() < webPageTwo.getScore()) {
            return 1;
        }
        if (webPageOne.getScore() > webPageTwo.getScore()) {
            return -1;
        }
        return webPageOne.getTitle().compareTo(webPageTwo.getTitle());
    };


    private List<String> getFieldName(List<LeafReaderContext> leaves) {
        List<String> fieldName = new ArrayList<>();
        for (LeafReaderContext subReader : leaves) {
            FieldInfos fields = subReader.reader().getFieldInfos();
            for (FieldInfo field : fields) {
                fieldName.add(field.name);
            }
        }
        return fieldName;
    }

    public List<WebPage> searchQuery(String searchQuery, int type) throws RepositoryError {

        result = new ArrayList<>();
        if (TEMP == null) {
            throw new RepositoryError(RepositoryErrorsEnum.REPOSITORY_IS_EMPTY);
        }

        try {
            Directory directory = FSDirectory.open(TEMP);
            Analyzer analyzer = new StandardAnalyzer();
            DirectoryReader ireader = DirectoryReader.open(directory);
            Formatter formatter = new SimpleHTMLFormatter();
            IndexSearcher isearcher = new IndexSearcher(ireader);

            for (String nameField : getFieldName(ireader.leaves())) {
                QueryParser parser = new QueryParser(nameField, analyzer);
                Query query = parser.parse(searchQuery.toLowerCase());
                QueryScorer highlighterScorer = new QueryScorer(query);
                Highlighter highlighter = new Highlighter(formatter, highlighterScorer);

                ScoreDoc[] hits = isearcher.search(query, 1, Sort.RELEVANCE, true).scoreDocs;
                if (hits.length > 0) {
                    Document doc = isearcher.doc(hits[0].doc);
                    TokenStream stream = TokenSources.getAnyTokenStream(ireader, hits[0].doc, nameField, analyzer);
                    String[] frags = highlighter.getBestFragments(stream, doc.get(nameField), 20);
                    String simpleText = frags[0];
                    simpleText = simpleText.replaceAll("<B>", "<mark>").replaceAll("</B>", "</mark>");
                    result.add(new WebPage(nameField.split("\n"), simpleText, hits[0].score));
                    if (result.size() == 10) {
                        break;
                    }
                }
            }
            ireader.close();
            directory.close();
        } catch (org.apache.lucene.index.IndexNotFoundException e) {
            e.printStackTrace();
            throw new RepositoryError(RepositoryErrorsEnum.REPOSITORY_IS_EMPTY);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sort(result, type);
    }

    @Override
    public List<WebPage> sort(List<WebPage> result, Integer type) {
        if (type == 0) {
            result.sort(relevantComparator);
            return result;
        }
        result.sort(abcComparator);
        return result;
    }
}
