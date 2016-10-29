package se;

import java.net.URL;
import java.util.LinkedList;

/**
 * Created by LKF on 2016/10/29.
 */
public class Result {
    // TODO: results { result, ... }

    public LinkedList<URL> url;
    public LinkedList<String> keyword;
    public LinkedList<String> ranking;

    public Result() {
        this.url = new LinkedList<>();
        this.keyword = new LinkedList<>();
        this.ranking = new LinkedList<>();
    }

    public String resultString() {
        String result = "";
        for (int i = 0; i < url.size(); i++) {
            result += url.toArray()[i].toString() + "\n" + keyword.toArray()[i] + "\n";
        }
        return result;
    }
}
