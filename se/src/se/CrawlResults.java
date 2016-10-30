package se;

import java.net.URL;
import java.util.LinkedList;

public class CrawlResults {
    public LinkedList<CrawlResult> results;

    public CrawlResults() {
        this.results = new LinkedList<>();
    }

    public String resultString() {
        StringBuilder sb = new StringBuilder();

        for (CrawlResult r : this.results) {
            sb.append(r.url + "\n" + r.keyword + "\n");
        }

        return sb.toString();
    }

    public void put(CrawlResult crawlResult) {
        this.results.add(crawlResult);
    }
}
