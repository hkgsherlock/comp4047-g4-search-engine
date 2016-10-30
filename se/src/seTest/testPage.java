package seTest;

import se.Page;
import se.PageKeywords;

import java.util.HashMap;
import java.util.HashSet;

public class testPage {
    public static void main(String[] args) throws Exception {
        Page page = Page.getFromRemote("https://hk.news.yahoo.com/%E9%A0%98%E5%B1%95%E6%A8%99%E5%94%AE5%E5%95%86%E5%A0%B4-%E4%BC%B0%E5%80%BC27%E5%84%84-223108942.html");

        PageKeywords pageKeywords = page.generateKeywords();
    }
}
