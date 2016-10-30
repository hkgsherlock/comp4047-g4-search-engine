package se;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PageKeywords {
    HashMap<String, PageKeywordPositions> pageKeywordsFrom = new HashMap<>();

    public void put(String from, PageKeywordPositions keywordsPositions) {
        if (pageKeywordsFrom.containsKey(from)) {
            pageKeywordsFrom.get(from).addAll(keywordsPositions);
        } else {
            pageKeywordsFrom.put(from, keywordsPositions);
        }
    }

    public Set<String> getFrom() {
        return pageKeywordsFrom.keySet();
    }

    public PageKeywordPositions get(String from) {
        return pageKeywordsFrom.get(from);
    }

    public Set<String> getAllKeywords() {
        HashSet<String> ret = new HashSet<>();

        for (Map.Entry<String, PageKeywordPositions> e : pageKeywordsFrom.entrySet()) {
            ret.addAll(e.getValue().getKeywords());
        }

        return ret;
    }

    public HashMap<String, KeywordUrlSourcePositions> convertToKeywordSourcePositions() {
        HashMap<String, KeywordUrlSourcePositions> ret = new HashMap<>();

        for (String from : getFrom()) {
            PageKeywordPositions pkp = get(from);

            for (String kw : pkp.getKeywords()) {
                if (!ret.containsKey(kw)) {
                    KeywordUrlSourcePositions kusp = new KeywordUrlSourcePositions(from);
                    ret.put(kw, kusp);
                }
                ret.get(kw).addAll(pkp.getSet(kw));
            }
        }

        return ret;
    }
}
