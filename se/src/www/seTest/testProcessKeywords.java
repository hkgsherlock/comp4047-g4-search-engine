package seTest;

import se.Page;
import se.ProcessKeywords;

import java.util.ArrayList;

public class testProcessKeywords {
    public static void main(String[] args) throws Exception {
        String text = Page.getFromRemote("https://hk.news.yahoo.com/%E9%A0%98%E5%B1%95%E6%A8%99%E5%94%AE5%E5%95%86%E5%A0%B4-%E4%BC%B0%E5%80%BC27%E5%84%84-223108942.html")
                .fetchTextDocumentWithBoilerpipe();
        ArrayList<String> strings = ProcessKeywords.fromTextual(text);
        System.out.println(String.join("\n", strings));

        String text2 = Page.getFromRemote("http://time.com/4550250/bob-dylan-nobel-prize-interview/")
                .fetchTextDocumentWithBoilerpipe();
        ArrayList<String> strings2 = ProcessKeywords.fromTextual(text2);
        System.out.println(String.join("\n", strings2));
    }
}
