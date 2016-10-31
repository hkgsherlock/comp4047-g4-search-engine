package se;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Keyword implements Serializable {
    String keyword;
    LinkedList<KeywordUrl> keywordUrls;

    public Keyword(String keyword) {
        this.keyword = keyword;
        this.keywordUrls = new LinkedList<>();
    }

    void addKeywordUrls(List<KeywordUrl> keywordUrls) {
        for (KeywordUrl ku : keywordUrls) {
            addKeywordUrl(ku);
        }
        keywordUrls.sort(KeywordUrl::compare);
    }

    void addKeywordUrl(KeywordUrl keywordUrl) {
        int idx = keywordUrls.indexOf(keywordUrl);
        if (idx > -1)
            keywordUrls.get(idx).addAll(keywordUrl.sources);
        else
            keywordUrls.add(keywordUrl);
        keywordUrls.sort(KeywordUrl::compare);
    }

    LinkedList<KeywordUrl> getAll() {
        return keywordUrls;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass() && this.keyword.equals(((Keyword) obj).keyword);
    }

    public void printHtml(){
        try {
            PrintStream printer = new PrintStream(System.out, true, "UTF-8");

            printer.println("<ol>");
            for (KeywordUrl k : keywordUrls) {
                printer.println("<li data-score=\"" + k.score + "\">");
                printer.println("<a href=\"" + k.url + "\">" + k.title + "</a><br/>");
                printer.println(k.url + "<br/>");
                printer.println(k.description);
                printer.println("</li>");
            }
            printer.println("</ol>");
        } catch (UnsupportedEncodingException e) {
        }
    }

    @Override
    public String toString() {
        return "{[Keyword] keyword=" + keyword + ", keywordUrls=" + keywordUrls.toString() + "}";
    }
}