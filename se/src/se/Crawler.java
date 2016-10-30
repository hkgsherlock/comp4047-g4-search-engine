package se;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

/**
 * Crawler is a class to do grabbing of all necessary urls from a url.
 */
public class Crawler {
    private String strUrlFrom;
    private CrawlResults crawlResults = new CrawlResults();
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

    // NOTTODO: if there's a start() there must be a threading thing -- nice bro (y)
    public void start() {
        try {
            URL urlFirstPage = new URL(strUrlFrom);

            Page firstPage = this.getFirstPage(urlFirstPage);
            addToResultUrl(firstPage.fetchUrlsInAElementsFromDocument(recursionPagesLimit));

//            System.out.println(crawlResults.resultString());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addToResultUrl(URL[] urls) throws IOException {
        Set<URL> urlSet = new HashSet<>(Arrays.asList(urls)); // TODO: means noting

        for (URL url : urlSet) {
            Page page = Page.getFromRemote(url);
            CrawlResult cr = new CrawlResult(page);
            crawlResults.put(cr);
        }

        KeywordsStorage.INSTANCE.sync();
    }

    private Page getFirstPage(String urlString) throws IOException {
        return this.getFirstPage(new URL(urlString));
    }

    private Page getFirstPage(URL url) throws IOException {
        Page page = Page.getFromRemote(url);

        // check if it contains meta:refresh AND with new page url
        Elements eleMetaRefresh = page.getDoc().select("meta[http-equiv=refresh]");
        if (eleMetaRefresh.size() > 0) {
            String[] contentDatas = eleMetaRefresh.attr("content").split("; *");
            for (String e : contentDatas) {
                String[] kv = e.split(" *= *");
                if (kv.length < 2)
                    continue;
                String key = kv[0];
                String value = kv[1];
                if (key.toLowerCase().equals("url")) {
                    return getFirstPage(value);
                }
            }
        }

        return page;
    }

    /**
     * test entry point for crawling http://www.hkbu.edu.hk
     */
    public static void main(String[] args) {
        /*if (0 == args.length) {
            System.exit(-1);
        }
        searchKeyword=args[0];*/
        searchKeyword = "test";

//        String start_url = "http://www.hkbu.edu.hk/eng/main/index.jsp";
        String start_url = "http://www.hkbu.edu.hk";
        int recursionPagesLimit = 10; // X
        int minNumResults = 100; // Y
        if (1 <= args.length) {
            start_url = args[0];
        }
        if (2 <= args.length) {
            minNumResults = Integer.parseInt(args[1]);
        }
        if (3 <= args.length) {
            recursionPagesLimit = Integer.parseInt(args[2]);
        }
        System.out.println("start crawling from " + start_url + " with Y=" + minNumResults + " and X=" + recursionPagesLimit);
        Crawler crawler = new Crawler(start_url, minNumResults, recursionPagesLimit);
        crawler.start();
    }
}