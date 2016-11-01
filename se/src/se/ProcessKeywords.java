package se;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessKeywords {
    public static ArrayList<String> fromTextual(String textual) {
        ArrayList<String> matches = new ArrayList<>();

        // https://www.regex101.com/r/ncxqj1/5
        // fetch digits, a-z, A-Z, Japanese and Chinese(and Kanji) characters
        String regexPattern = "[a-zA-Z\\u3040-\\u30ff\\u31F0-\\u31FF\\u3400-\\u9fa5]+";
        Pattern pattern = Pattern.compile(regexPattern, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(textual);
        while (matcher.find()) {
            matches.add(matcher.group(0));
        }

        return matches;
    }

    //    @Deprecated
    public static PageKeywordPositions fromTextualWithPosition(String textual) {
        PageKeywordPositions ret = new PageKeywordPositions();

        ArrayList<String> keywords = fromTextual(textual);
        for (int i = 0; i < keywords.size(); i++) {
            String kw = keywords.get(i);
            ret.add(kw, i);
        }

        // filter connection words
        String[] filterKeywords = new String[] {"and", "the", "for", "did", "does", "are", "was", "were", "has", "have", "had", "that", "this", "these", "which", "whose", "who", "whom", "what", "why", "she", "they", "them"};
        ret.filter(filterKeywords);

        // filter keywords not with at least 3 alphabets
        ret.filterRegex(".?.?"); // 've already done rejection on keywords storage (?)

        return ret;
    }
}
