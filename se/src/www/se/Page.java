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
import java.util.HashSet;

public class Page {
    private static final int DEFAULT_TIMEOUT = 30000;

    private HttpResult httpResult;
    private Document doc;
    long timeFetch = -1;

    private String cacheKeywords = "";

    public Page(String html) {
        this.doc = Jsoup.parse(html);
    }

    public Page(Document doc) {
        this.doc = doc;
    }

    public Page(String html, HttpResult HttpResult) {
        this.doc = Jsoup.parse(html);
        this.httpResult = HttpResult;
    }

    public Page(Document doc, HttpResult httpResult) {
        this.doc = doc;
        this.httpResult = httpResult;
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

    private String fetchTextualDataAsKeywords() {
        return this.doc.body().text().substring(0, 100) + "...";
    }

    private String fetchMetaKeywordContent() {
        return this.doc.select("meta[keywords]").attr("content");
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

    private String fetchWholeBodyText() {
        return this.doc.body().text();
    }

    private String fetchTextDocumentWithBoilerpipe() throws SAXException, BoilerpipeProcessingException {
        InputSource is = new InputSource(new ByteArrayInputStream(this.getHtml().getBytes(StandardCharsets.UTF_8)));
        BoilerpipeSAXInput in = new BoilerpipeSAXInput(is);
        TextDocument textDocument = in.getTextDocument();
        return ArticleExtractor.INSTANCE.getText(textDocument);
    }

    public String generateKeywords() {
        if (cacheKeywords == null)
            cacheKeywords = fetchMetaKeywordContent() + "\n" + fetchTextualDataAsKeywords();
        return cacheKeywords;
    }

    public String countRanking() {
        String searchKeyword = this.generateKeywords();

        int score = 0;
        Elements paragraphs = doc.select("p");

        //if there are containing keyword in paragraphs add 3 score
        for (Element p : paragraphs) {
            if (p.toString().contains(searchKeyword)) {
                score += 3;
            }
        }

        //if the folder of the url containing keyword add 30 score
        {
            String[] urlDirectories = this.getUrl().getFile().split("/");
            for (String dir : urlDirectories) {
                if (dir.contains(searchKeyword)) {
                    score += 30;
                    break;
                }
            }
        }

        //if the domain name and url containing keyword add 20 score
        if (doc.baseUri().substring(doc.baseUri().indexOf("/") + 1).contains(searchKeyword)) {
            score += 20;
        }
        if (score > 80) {
            return "A";
        } else if (score >= 70) {
            return "B";
        } else if (score >= 60) {
            return "C";
        } else if (score >= 40) {
            return "D";
        } else if (score >= 20) {
            return "E";
        } else return "F";
    }
}
