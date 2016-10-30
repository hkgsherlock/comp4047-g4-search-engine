package se;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Page {
    private static final int DEFAULT_TIMEOUT = 30000;

    private HttpResult httpResult;
    private Document doc;
    long timeFetch = -1;

    private String cacheBoilerpipeText = null;
    private String cacheJsoupWholeBodyText = null;

    public Page(String html) {
        this.doc = Jsoup.parse(html);
    }

    public Page(Document doc) {
        this.doc = doc;
    }

    private Page(String html, HttpResult HttpResult) {
        this.doc = Jsoup.parse(html);
        this.httpResult = HttpResult;
    }

    private Page(Document doc, HttpResult httpResult) {
        this.doc = doc;
        this.httpResult = httpResult;
    }

    public Page(HttpResult httpResult) throws IOException {
        this(httpResult.readTexts(), httpResult);
    }

    public String getHtml() {
        return this.doc.outerHtml();
    }

    public Document getDoc() {
        return this.doc;
    }

    public URL getUrl() {
        return this.httpResult.getHttpConnection().getURL();
    }

    public long getTimeConnect() {
        return this.httpResult.getTimeConnect();
    }

    public void setTimeFetch(int timeFetch) {
        this.timeFetch = timeFetch;
    }

    public static Page getFromRemote(String urlString) throws IOException {
        return getFromRemote(urlString, DEFAULT_TIMEOUT);
    }

    public static Page getFromRemote(String urlString, int timeFetch) throws IOException {
        return getFromRemote(new URL(urlString), timeFetch);
    }

    public static Page getFromRemote(URL url) throws IOException {
        return getFromRemote(url, DEFAULT_TIMEOUT);
    }

    public static Page getFromRemote(URL url, int timeout) throws IOException {
        HttpResult httpResult = Http.get(url, timeout);
        return Page.getFromHttpResult(httpResult);
    }

    public static Page getFromHttpResult(HttpResult httpResult) throws IOException {
        if (!httpResult.getContentType().equals("text/html"))
            return null;
        return new Page(httpResult.readTexts(), httpResult);
    }

    private URL[] fetchUrlsInAElementsFromDocument() {
        return fetchUrlsInAElementsFromDocument(0);
    }

    URL[] fetchUrlsInAElementsFromDocument(int maxResults) {
        HashSet<URL> urls = new HashSet<URL>();

        Elements a = doc.select("a[href]");

        for (Element ea : a) {
            // "abs:" means not to fetch anchor urls or javascript links etc
            String url_extracted = ea.attr("href");

            // if no extraction then pass
            if (url_extracted.length() == 0 ||
                    !(url_extracted.startsWith("https://") ||
                            url_extracted.startsWith("http://") ||
                            url_extracted.startsWith("/") ||
                            url_extracted.startsWith("."))
                    )
                continue;

            // putting url
            try {
                URL urlUrlExtracted;

                if (url_extracted.startsWith("https://") || url_extracted.startsWith("http://")) {
                    urlUrlExtracted = new URL(url_extracted);
                } else {
                    urlUrlExtracted = new URL(getUrl().getProtocol(), getUrl().getHost(), getUrl().getPort(), url_extracted);
                }

                if (urlUrlExtracted.equals(getUrl()))
                    continue;

                urls.add(urlUrlExtracted);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (maxResults > 0 && urls.size() >= maxResults)
                break;
        }

        return urls.toArray(new URL[urls.size()]);
    }

    @Deprecated
    private String fetchMetaKeyword() {
        return this.doc.select("meta[keywords]").attr("content");
    }

    private String fetchMetaDescription() {
        return this.doc.select("meta[description]").attr("content");
    }

    private String[] fetchPText() {
        ArrayList<String> texts = new ArrayList<String>();
        Elements p = doc.select("p");
        for (Element ep : p) {
            String text = ep.text();
            if (text.length() > 0)
                continue;
            texts.add(text);
        }
        return (String[]) texts.toArray();
    }

    private String fetchPageTitle() {
        return doc.title();
    }

    private String fetchWholeBodyText() {
        if (cacheJsoupWholeBodyText == null) {
            cacheJsoupWholeBodyText = this.doc.body().text();
        }
        return cacheJsoupWholeBodyText;
    }

    public String fetchTextDocumentWithBoilerpipe() throws SAXException, BoilerpipeProcessingException {
        if (cacheBoilerpipeText == null) {
            InputSource is = new InputSource(new ByteArrayInputStream(this.getHtml().getBytes(StandardCharsets.UTF_8)));
            BoilerpipeSAXInput in = new BoilerpipeSAXInput(is);
            TextDocument textDocument = in.getTextDocument();
            cacheBoilerpipeText = ArticleExtractor.INSTANCE.getText(textDocument);
        }
        return cacheBoilerpipeText;
    }

    public PageKeywords generateKeywords() {
        PageKeywords pk = new PageKeywords();

        // meta keyword -- no we don't use this anymore https://sofree.cc/meta-keywords/

        // meta description
        {
            String metaDesc = fetchMetaDescription();
            pk.put("Meta Description", ProcessKeywords.fromTextualWithPosition(metaDesc));
        }

        // textual data
        {
            String txtBoilerpipe = "";
            try {
                txtBoilerpipe = this.fetchTextDocumentWithBoilerpipe();
            } catch (Exception e) {
            }
            String txtJsoup = this.fetchWholeBodyText();

            PageKeywordPositions keywordsPositions = null;

            if (
                    (doc.select("article").size() > 0 || doc.select("section").size() > 0) &&
                            ((double) txtBoilerpipe.length() / (double) txtJsoup.length() * 100.0) > 10.0) {
                // use boilerpipe
                keywordsPositions = ProcessKeywords.fromTextualWithPosition(txtBoilerpipe);
            } else {
                // use jsoup
                keywordsPositions = ProcessKeywords.fromTextualWithPosition(txtJsoup);
            }

            pk.put("Text", keywordsPositions);
        }

        return pk;
    }

    // TODO: return Set<Keyword>?
    public Set<Keyword> generateKeywordsAndCountScore() {
        HashSet<Keyword> keywords = new HashSet<>();

        PageKeywords pageKeywords = generateKeywords();
        HashMap<String, KeywordUrlSourcePositions> kusp = pageKeywords.convertToKeywordSourcePositions();
        for (String kw : pageKeywords.getAllKeywords()) {
            int score = _countScore(kw);

            Keyword keyword = new Keyword(kw);
            KeywordUrl keywordUrl = new KeywordUrl(getUrl().toString(), score);
            keywordUrl.addAll(kusp.values());
            keyword.addKeywordUrl(keywordUrl);
            keywords.add(keyword);
        }

        // TODO: store into keyword storage?
        return keywords;
    }

    private int _countScore(String searchKeyword) {
        int score = 0;

        //if there are containing keyword in paragraphs put 3 score
        {
            String[] paragraphs = fetchPText();
            for (String p : paragraphs) {
                if (p.contains(searchKeyword)) {
                    score += 3;
                }
            }
        }

        //if the folder of the url containing keyword put 30 score
        {
            String[] urlDirectories = this.getUrl().getFile().split("/");
            for (String dir : urlDirectories) {
                if (dir.contains(searchKeyword)) {
                    score += 30;
                    break;
                }
            }
        }

        //if the domain name and url containing keyword put 20 score
        if (doc.baseUri().substring(doc.baseUri().indexOf("/") + 1).contains(searchKeyword)) {
            score += 20;
        }

        return score;
    }
}
