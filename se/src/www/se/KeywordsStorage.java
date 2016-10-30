package se;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;

public class KeywordsStorage {
    public static final KeywordsStorage INSTANCE = new KeywordsStorage();

    private HashMap<String, Keyword> keywords;
    private HashSet<String> _removedYetDeleted;

    public KeywordsStorage() {
        this.keywords = new HashMap<>();
        this._removedYetDeleted = new HashSet<>();
    }

    public void sync() throws IOException {
        for (String kw : keywords.keySet()) {
            File fileKw = new File("keywords/" + kw, true);
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fileKw.getOutputStream()))) {
                oos.writeObject(keywords.get(kw));
            }
        }
        for (String kwRmv : _removedYetDeleted) {
            File fileKwRmv = new File("keywords/" + kwRmv, true);
            fileKwRmv.delete();
        }
        _removedYetDeleted.clear();
    }

    public void put(Keyword keyword) {
        this.keywords.put(keyword.keyword, keyword);
    }

    public Keyword get(String keywordString) {
        return this.keywords.get(keywordString);
    }

    public Keyword remove(String keywordString) {
        if (!keywords.containsKey(keywordString))
            return null;
        _removedYetDeleted.add(keywordString);
        return this.keywords.remove(keywordString);
    }
}
