package se;

import java.io.Serializable;
import java.util.*;

public class KeywordUrl implements Serializable {
    LinkedList<KeywordUrlSourcePositions> sources = new LinkedList<>();
    String url;
    int score;

    public KeywordUrl(String url) {
        this(url, 0);
    }

    public KeywordUrl(String url, int score) {
        this.url = url;
        this.score = score;
        this.sources = new LinkedList<>();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass() && this.url.equals(((KeywordUrl) obj).url);
    }

    public void add(KeywordUrlSourcePositions value) {
        this.sources.add(value);
    }

    public void addAll(Collection<KeywordUrlSourcePositions> values) {
        for (KeywordUrlSourcePositions v : values) {
            int idx = sources.indexOf(v);
            if (idx > -1) {
                sources.get(idx).addAll(v.positions);
            } else {
                sources.add(v);
            }
        }
    }
}
