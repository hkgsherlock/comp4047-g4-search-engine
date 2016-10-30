package se;

import java.io.Serializable;
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
            int idx = keywordUrls.indexOf(ku);
            if (idx > -1)
                keywordUrls.get(idx).addAll(ku.sources);
            else
                keywordUrls.add(ku);
        }
    }

    void addKeywordUrl(KeywordUrl keywordUrl) {
        keywordUrls.add(keywordUrl);
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass() && this.keyword.equals(((Keyword) obj).keyword);
    }
}