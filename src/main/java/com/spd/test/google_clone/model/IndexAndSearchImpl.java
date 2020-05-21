package com.spd.test.google_clone.model;

import com.spd.test.google_clone.errors.RepositoryException;
import lombok.RequiredArgsConstructor;
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
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.DirectoryReader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


@org.springframework.stereotype.Repository
@RequiredArgsConstructor
public class IndexAndSearchImpl implements IndexAndSearch {
    private final Path getRepositoryStoragePath;

    private List<WebPage> result;
    private static final Logger log = LoggerFactory.getLogger(IndexAndSearchImpl.class);

    public void init() {
        File[] files = new File(getRepositoryStoragePath.toString()).listFiles();

        if(files!= null && files.length > 0) {
            for (File myFile : files)
                if (myFile.isFile()) {
                    try {
                        Files.delete(myFile.toPath());
                    } catch (IOException e) {
                        log.error("Can`t delete files in temp directory");
                    }

                }
        }
    }

    private Document createDocument(String url, String text) {
        Document document = new Document();
        document.add(new Field(url, text, TextField.TYPE_STORED));
        return document;
    }

    public synchronized void indexingPage(String urlAndTitle, String text) {
        Document document = createDocument(urlAndTitle, text);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig  config = new IndexWriterConfig(analyzer);
        try(Directory directory = FSDirectory.open(getRepositoryStoragePath);
            IndexWriter iWriter = new IndexWriter(directory, config)) {
        iWriter.addDocument(document);
           iWriter.commit();
        } catch (IOException e) {
            log.error("IO Exception in indexingPage",e);
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

    private Optional<String> setHighlighter(Highlighter highlighter, Analyzer analyzer, String name, String text) throws IOException, InvalidTokenOffsetsException {
        String[] frags = highlighter.getBestFragments(analyzer, name, text,5);
        if(frags.length == 0){return Optional.empty();}
        return Optional.of(frags[0]);
    }

    public List<WebPage> searchQuery(String searchQuery, SortType type) throws RepositoryException {

        result = new ArrayList<>();
        if (getRepositoryStoragePath == null) {
            throw new RepositoryException("The repository base is empty, try indexing the site first");
        }

        try(Directory directory = FSDirectory.open(getRepositoryStoragePath);
            DirectoryReader reader = DirectoryReader.open(directory);
            Analyzer analyzer = new StandardAnalyzer()) {
            Formatter formatter = new SimpleHTMLFormatter();
            IndexSearcher searcher = new IndexSearcher(reader);
            for (String nameField : getFieldName(reader.leaves())) {
                QueryParser parser = new QueryParser(nameField, analyzer);
                Query query = parser.parse(searchQuery.toLowerCase());
                QueryScorer highlighterScorer = new QueryScorer(query);
                Highlighter highlighter = new Highlighter(formatter, highlighterScorer);
                ScoreDoc[] hits = searcher.search(query, 1, Sort.RELEVANCE, true).scoreDocs;
                if (hits.length > 0) {
                    Document doc = searcher.doc(hits[0].doc);
                    Optional<String> text = setHighlighter(highlighter,analyzer,nameField,doc.get(nameField));
                    if(text.isPresent()){
                        String simpleText = text.get().replace("<B>", "<mark>").replace("</B>", "</mark>");
                        result.add(new WebPage(nameField.split("\n"), simpleText, hits[0].score));

                    }
                    if (result.size() == 10) {
                        break;
                    }
                }
            }

        } catch (org.apache.lucene.index.IndexNotFoundException e) {
          log.error("Error in searchQuery: repository is empty",e);
            throw new RepositoryException("The repository base is empty, try indexing the site first");

        } catch (IOException e) {
            log.error("Error in searchQuery: can`t open directory",e);
        } catch (InvalidTokenOffsetsException |ParseException e) {
            log.error("Error in searchQuery: can`t parse query",e);
        }


        return sort(result, type);
    }

    @Override
    public List<WebPage> sort(List<WebPage> result, SortType type) {
        if (type.equals(SortType.RELEVANT)) {
            result.sort(relevantComparator);
            return result;
        }
        result.sort(abcComparator);
        return result;

    }
}
