package manos.examples.apache.lucene;

import mt.streams.StreamUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A document is a collection of fields. Example:
 * Doc1:
 * title: Actual Document Title
 * body: The body of the document
 * title and body are the field names and are custom
 *
 * Analyzers - the analyzers are available for a lot of languages
 *  StandardAnalyzer – analyses based on basic grammar, removes stop words like “a”, “an” etc. Also converts in lowercase
 *  SimpleAnalyzer – breaks the text based on no-letter character and converts in lowercase
 *  WhiteSpaceAnalyzer – breaks the text based on white spaces
 *
 * Query Syntax:
 * In gerenal the syntax is:
 *  field_name: search_text
 *
 *  we can use ranges like: timestamp:[1509909322,1572981321]
 *  we can use wildcards: title: dr?nk or d*k for single and multiple characters
 *
 *  we can also use AND, NOT and OR operators like body: "text_to_search1" AND "text_to_search2"
 *
 */
public class InMemoryLuceneTest {
    static final String TITLE_FIELD = "title";
    static final String BODY_FIELD = "body";
    Directory memoryDir;
    StandardAnalyzer analyzer = new StandardAnalyzer();

    @Before
    public void before() {
        memoryDir = new RAMDirectory();
    }
    private Document createDocument(String title, String body){
        Document document = new Document();
        document.add(new TextField(TITLE_FIELD, title, Field.Store.YES));
        document.add(new SortedDocValuesField(TITLE_FIELD, new BytesRef(title)));
        document.add(new TextField(BODY_FIELD, body, Field.Store.YES));
        return document;
    }
    private void addDocumentsToIndex(List<Document> documents){
        try (final IndexWriter indexWriter = new IndexWriter(memoryDir, new IndexWriterConfig(analyzer))){
            documents.forEach(StreamUtils.rethrowConsumer(indexWriter::addDocument));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void deleteDocumentFromIndex(Query... queries){
        try (final IndexWriter indexWriter = new IndexWriter(memoryDir, new IndexWriterConfig(analyzer))){
            indexWriter.deleteDocuments(queries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reader Hierarchy
     * java.lang.Object
     *  org.apache.lucene.index.IndexReader
     *      org.apache.lucene.index.CompositeReader
     *          org.apache.lucene.index.BaseCompositeReader<LeafReader>
     *                  org.apache.lucene.index.DirectoryReader
     */
    private List<Document> search(Query query, Sort sort) throws IOException {
        DirectoryReader indexReader = DirectoryReader.open(memoryDir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(query, indexReader.maxDoc(), sort);
        return Arrays.stream( topDocs.scoreDocs)
                .map(StreamUtils.rethrowFunction(scoreDoc -> indexSearcher.doc(scoreDoc.doc)))
                .collect(Collectors.toList());
    }
    private List<Document> search(Query query) throws IOException {
        DirectoryReader indexReader = DirectoryReader.open(memoryDir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(query, indexReader.maxDoc());
        return Arrays.stream( topDocs.scoreDocs)
                .map(StreamUtils.rethrowFunction(scoreDoc -> indexSearcher.doc(scoreDoc.doc)))
                .collect(Collectors.toList());
    }

    @Test
    public void simpleQuery() throws ParseException, IOException {
        addDocumentsToIndex(Arrays.asList(
                createDocument("title0", "hello lucene"),
                createDocument("title1", "hello again")));

        Query query = new QueryParser(BODY_FIELD, analyzer).parse("lucene");

        List<Document> searchResults = search(query);

        Assert.assertEquals(1, searchResults.size());
        Assert.assertEquals("hello lucene", searchResults.get(0).get(BODY_FIELD));
    }

    @Test
    /**
     * A Term is a basic unit for search, containing the field name together with the text to be searched for.
     * TermQuery is the simplest of all queries consisting of a single term:
     */
    public void termQuery() throws IOException {
        addDocumentsToIndex(Arrays.asList(
                createDocument("title0", "hello lucene"),
                createDocument("title1", "hello again")));
        Query query = new TermQuery(new Term(BODY_FIELD, "lucene"));

        List<Document> searchResults = search(query);

        Assert.assertEquals(1, searchResults.size());
        Assert.assertEquals("hello lucene", searchResults.get(0).get(BODY_FIELD));
    }

    @Test
    /**
     * To search a document with a “starts with” word:
     */
    public void prefixQuery() throws IOException {
        addDocumentsToIndex(Arrays.asList(
                createDocument("title0", "hello lucene"),
                createDocument("title1", "hello again")));
        Query query = new PrefixQuery(new Term(BODY_FIELD, "hello"));

        List<Document> searchResults = search(query);

        Assert.assertEquals(2, searchResults.size());
        Assert.assertEquals("hello lucene", searchResults.get(0).get(BODY_FIELD));
    }

    @Test
    /**
     *As the name suggests, we can use wildcards “*” or “?” for searching
     */
    public void wildcardQuery() throws IOException {
        addDocumentsToIndex(Arrays.asList(
                createDocument("title0", "hello lucene"),
                createDocument("title1", "hello lutene")));
        Query query = new WildcardQuery(new Term(BODY_FIELD, "hello*"));
        List<Document> searchResults = search(query);

        Assert.assertEquals(2, searchResults.size());
        Assert.assertEquals("hello lucene", searchResults.get(0).get(BODY_FIELD));

        query = new WildcardQuery(new Term(BODY_FIELD, "lu?ene"));
        searchResults = search(query);
        Assert.assertEquals(2, searchResults.size());
        Assert.assertEquals("hello lutene", searchResults.get(1).get(BODY_FIELD));
    }

    @Test
    /**
     * Phrase Query is used to search a sequence of texts in a document
     */
    public void phraseQuery() throws IOException {
        addDocumentsToIndex(Arrays.asList(
                createDocument("title0", "roses are red violets are blue, here is a lucene phrase example, I did it for you"),
                createDocument("title1", "roses are red I may told a lie but I do not care code 4 life")));

        Query query = new PhraseQuery(2, BODY_FIELD, new BytesRef("roses"), new BytesRef("violets"));
        List<Document> searchResults = search(query);

        Assert.assertEquals(1, searchResults.size());
        Assert.assertEquals("title0", searchResults.get(0).get(TITLE_FIELD));
    }

    @Test
    /**
     * Fuzzy Query is used to search something similar
     */
    public void fuzzyQuery() throws IOException {
        addDocumentsToIndex(Arrays.asList(
                createDocument("title0", "manos tzagkarakis"),
                createDocument("title1", "manos tsagarakis")));

        Query query = new FuzzyQuery(new Term(BODY_FIELD, "tzagkarakis"));
        List<Document> searchResults = search(query);

        Assert.assertEquals(2, searchResults.size());
        Assert.assertEquals("title0", searchResults.get(0).get(TITLE_FIELD));
    }

    @Test
    /**
     * Boolean Query is used when we need boolean operators
     */
    public void booleanQuery() throws IOException {
        addDocumentsToIndex(Arrays.asList(
                createDocument("title0", "roses are red violets are blue, here is a lucene boolean example, I did it for you"),
                createDocument("title1", "roses are red this may be scary but lucene is easy with boolean query")));

        Query query = new BooleanQuery.Builder()
                .add(new TermQuery(new Term(BODY_FIELD, "roses")), BooleanClause.Occur.MUST)
                .add(new TermQuery(new Term(BODY_FIELD, "boolean")), BooleanClause.Occur.MUST)
                .build();
        List<Document> searchResults = search(query);

        Assert.assertEquals(2, searchResults.size());
        Assert.assertEquals("title1", searchResults.get(0).get(TITLE_FIELD));
    }

    @Test
    /**
     * Sorting search results
     * //had to add document.add(new SortedDocValuesField(TITLE_FIELD, new BytesRef(title))); to document creation
     */
    public void shortSearchResultAfterQuery() throws IOException {
        addDocumentsToIndex(Arrays.asList(
                createDocument("title0", "hello lucene"),
                createDocument("title1", "hello again"),
                createDocument("title2", "hello again again")));

        Query query = new TermQuery(new Term(BODY_FIELD, "hello"));
        Sort sort = new Sort(new SortField(TITLE_FIELD, SortField.Type.STRING, false));
        List<Document> searchResults = search(query, sort);

        Assert.assertEquals(3, searchResults.size());
        Assert.assertEquals("title0", searchResults.get(0).get(TITLE_FIELD));
    }

    @Test
    public void deleteDocumentFromIndexWithQuery(){
        addDocumentsToIndex(Arrays.asList(
                createDocument("title0", "hello lucene"),
                createDocument("title1", "hello again"),
                createDocument("title2", "hello again again")));

        Query query = new TermQuery(new Term(BODY_FIELD, "again"));
        deleteDocumentFromIndex(query);

        try (final DirectoryReader dr = DirectoryReader.open(memoryDir)) {
            Assert.assertEquals(1, dr.numDocs());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
