package se;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class PageKeywordPositions {
    private HashMap<String, HashSet<Integer>> keywordPositions = new HashMap<>(); // keyword, positions

    public void add(String keyword, int position) {
        if (keyword.length() < 3 || Pattern.matches("(^[\\d\\.]+$|^\\d|\\d$)", keyword))
            return;

        keyword = keyword.toLowerCase();

        if (this.keywordPositions.containsKey(keyword)) {
            this.keywordPositions.get(keyword).add(position);
        } else {
            HashSet<Integer> newPositions = new HashSet<>();
            newPositions.add(position);
            this.keywordPositions.put(keyword, newPositions);
        }
    }

    public void addAll(String keyword, Set<Integer> positions) {
        if (keyword.length() < 3 || Pattern.matches("(^[\\d\\.]+$|^\\d|\\d$)", keyword))
            return;

        keyword = keyword.toLowerCase();

        if (this.keywordPositions.containsKey(keyword)) {
            this.keywordPositions.get(keyword).addAll(positions);
        } else {
            HashSet<Integer> newPositions = new HashSet<>();
            newPositions.addAll(positions);
            this.keywordPositions.put(keyword, newPositions);
        }
    }

    public void addAll(PageKeywordPositions pageKeywordPositions) {
        for (String kw : pageKeywordPositions.getKeywords()) {
            this.addAll(kw, pageKeywordPositions.getSet(kw));
        }
    }

    public HashSet<Integer> getSet(String keyword) {
        if (!has(keyword)) {
            return null;
        }
        return this.keywordPositions.get(keyword);
    }

    public boolean has(String keyword) {
        return this.keywordPositions.containsKey(keyword);
    }

    public Set<String> getKeywords() {
        return keywordPositions.keySet();
    }
}
