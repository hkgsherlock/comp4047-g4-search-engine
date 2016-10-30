package se;

import com.sun.org.apache.xpath.internal.compiler.Keywords;

import java.net.URL;
import java.util.Collection;
import java.util.Set;

public class CrawlResult {
    URL url;
    Collection<Keyword> keyword;

    public CrawlResult(URL url, Set<Keyword> keyword) {
        this.url = url;
        this.keyword = keyword;
    }

    public CrawlResult(Page page) {
        this.url = page.getUrl();
        this.keyword = page.generateKeywordsAndCountScore();
    }
}
