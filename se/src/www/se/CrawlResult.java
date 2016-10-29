package se;

import java.net.URL;

public class CrawlResult {
    URL url;
    String keyword;
    String ranking;

    public CrawlResult(URL url, String keyword, String ranking) {
        this.url = url;
        this.keyword = keyword;
        this.ranking = ranking;
    }

    public CrawlResult(Page page) {
        this.url = page.getUrl();
        this.keyword = page.generateKeywords();
        this.ranking = page.countRanking();
    }
}
