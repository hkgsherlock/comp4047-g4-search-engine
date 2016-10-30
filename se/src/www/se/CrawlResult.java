package se;

import com.sun.org.apache.xpath.internal.compiler.Keywords;

import java.net.URL;
import java.util.Set;

public class CrawlResult {
    URL url;
    Set<Keywords> keyword;

    public CrawlResult(URL url, Set<Keywords> keyword) {
        this.url = url;
        this.keyword = keyword;
    }

    public CrawlResult(Page page) {
        this.url = page.getUrl();
        this.keyword = page.generateKeywords().getAllKeywords();
    }
}
