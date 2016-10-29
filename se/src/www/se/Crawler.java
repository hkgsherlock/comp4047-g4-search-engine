package se;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Crawler is a class to do grabbing of all necessary urls from a url.
 */
public class Crawler {
    private String strUrlFrom;
    private Result result = new Result();
    private static String searchKeyword = "";
    private int minNumResults = -1;
    private int recursionPagesLimit = -1;

    /**
     * To crawl a web page with its &lt;a&gt; URLs with specified recursion level and minimum number of results.
     *
     * @param urlString           The URL of web page to crawl.
     * @param minNumResults       The minimum number of results.
     * @param recursionPagesLimit How many recursion to run on crawled URLs from the first web page.
     */
    public Crawler(String urlString, int minNumResults, int recursionPagesLimit) {
        this.strUrlFrom = urlString;
        this.minNumResults = minNumResults;
        this.recursionPagesLimit = recursionPagesLimit;
    }

    // TODO: if there's a start() there must be a threading thing -- nice bro @yipeipei (y)
    public void start() {
        FileIO fd = new FileIO();
        try {
            URL urlFirstPage = new URL(strUrlFrom);

            String fileName = urlFirstPage.getHost().replace('.', '_'); // www_hkbu_edu_hk

            Document docFirstPage = this.getFirstPage(urlFirstPage);
            addToResultUrl(fetchUrlsInAElementsFromDocument(docFirstPage, recursionPagesLimit));

            fd.write(fileName, result.resultString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addToResultUrl(URL[] urls) throws IOException {
        for (URL url : urls) {
            // TODO: no this fuck shit
            if (result.url.indexOf(url) == -1) { //if the url is not repeated
                result.url.add(url);
                Document doc = getFirstPage(url);
                result.keyword.add(fetchMetaKeywordContent(doc) + "\n" + fetchTextualDataAsKeywords(doc));
                result.ranking.add(countingRanking(doc, searchKeyword));
            }
        }
    }

    private String countingRanking(Document doc, String searchKeyword) throws IOException {
        int score = 0;
        Elements paragraphs = doc.select("p");
        //if there are containing keyword in paragraphs add 3 score
        for (Element p : paragraphs) {
            if (p.toString().contains(searchKeyword)) {
                score += 3;
            }
        }
        //if the folder of the url containing keyword add 30 score
        String pathOfWeb = doc.baseUri().substring(doc.baseUri().indexOf("/") + 2);
        pathOfWeb = pathOfWeb.substring(pathOfWeb.indexOf("/"));
        if (pathOfWeb.contains(searchKeyword)) {
            score += 30;
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

    private Document getFirstPage(String urlString) throws IOException {
        return this.getFirstPage(new URL(urlString));
    }

    private Document getFirstPage(URL url) throws IOException {
        Document startingPage = this.getPage(url);

        // check if it contains meta:refresh AND with new page url
        Elements eleMetaRefresh = startingPage.select("meta[http-equiv=refresh]");
        if (eleMetaRefresh.size() > 0) {
            String[] contentDatas = eleMetaRefresh.attr("content").split("; *");
            for (String e : contentDatas) {
                String[] kv = e.split(" *= *");
                if (kv.length < 2) continue;
                String key = kv[0];
                String value = kv[1];
                if (key.toLowerCase().equals("url")) {
                    return getFirstPage(value);
                }
            }
        }

        return startingPage;
    }

    private Document getPage(String urlString) throws IOException {
        return this.getPage(new URL(urlString));
    }

    private Document getPage(URL url) throws IOException {
        return Jsoup.parse(url, 10000);
    }

    private URL[] fetchUrlsInAElementsFromDocument(Document doc) {
        return fetchUrlsInAElementsFromDocument(doc, 0);
    }

    private URL[] fetchUrlsInAElementsFromDocument(Document doc, int maxResults) {
        ArrayList<URL> urls = new ArrayList<URL>();

        Elements a = doc.select("a[href]");

        int fetchedCount = 0;
        for (Element ea : a) {
            // "abs:" means not to fetch anchor urls or javascript links etc
            String url_extracted = a.attr("abs:href");

            // if no extraction then pass
            if (url_extracted.length() == 0)
                continue;

            // putting url
            try {
                urls.add(new URL(url_extracted));
                fetchedCount++;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (maxResults > 0 && fetchedCount > maxResults)
                break;
        }

        return urls.toArray(new URL[urls.size()]);
    }

    private String fetchTextualDataAsKeywords(Document doc) {
        return doc.body().text().substring(0, 100) + "...";
    }

    private String fetchMetaKeywordContent(Document doc) {
        return doc.select("meta[keywords]").attr("content");
    }

    private String[] fetchPText(Document doc) {
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

    /**
     * test entry point for craeling http://www.hkbu.edu.hk
     */
    public static void main(String[] args) {
        /*if (0 == args.length) {
            System.exit(-1);
        }
        searchKeyword=args[0];*/
        searchKeyword = "test";

//        String start_url = "http://www.hkbu.edu.hk/eng/main/index.jsp";
        String start_url = "http://www.hkbu.edu.hk";
        int Y = -1;
        int X = -1;
        if (1 <= args.length) {
            start_url = args[0];
        }
        if (2 <= args.length) {
            Y = Integer.parseInt(args[1]);
        }
        if (3 <= args.length) {
            X = Integer.parseInt(args[2]);
        }
        System.out.println("start crawling from " + start_url + " with Y=" + Y + " and X=" + X);
        Crawler crawler = new Crawler(start_url, Y, X);
        crawler.start();
    }
}