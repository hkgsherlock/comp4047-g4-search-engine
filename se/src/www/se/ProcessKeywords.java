package se;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessKeywords {
    public static ArrayList<String> fromTextual(String textual) {
        ArrayList<String> matches = new ArrayList<>();

        // https://www.regex101.com/r/ncxqj1/1
        /*
        // TODO: describe each part of regex?
         */
        String regexPattern = "(\\d+\\.\\d+|[a-zA-Z0-9\\u4E00-\\u9fa5\\uE7C7-\\uE7F3])+";
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
            ret.add(keywords.get(i), i);
        }

        return ret;
    }
}
