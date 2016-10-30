package se;

import java.io.Serializable;
import java.util.*;

public class KeywordUrl implements Serializable, Comparable<KeywordUrl> {
    LinkedList<KeywordUrlSourcePositions> sources = new LinkedList<>();
    String url;
    int score;
    String description;

    public KeywordUrl(String url) {
        this(url, 0);
    }

    public KeywordUrl(String url, int score) {
        this.url = url;
        this.score = score;
        this.sources = new LinkedList<>();
    }

    public void add(KeywordUrlSourcePositions value) {
        int idx = sources.indexOf(value);
        if (idx > -1) {
            sources.get(idx).addAll(value.positions);
        } else {
            sources.add(value);
        }
    }

    public void addAll(Collection<KeywordUrlSourcePositions> values) {
        for (KeywordUrlSourcePositions v : values) {
            add(v);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass() && this.url.equals(((KeywordUrl) obj).url);
    }

    @Override
    public int compareTo(KeywordUrl another) {
        return compare(this, another);
    }

    public static int compare(KeywordUrl o1, KeywordUrl o2) {
        int x = o1.score;
        int y = o2.score;
        return (x > y) ? -1 : ((x == y) ? 0 : 1);
    }
}
