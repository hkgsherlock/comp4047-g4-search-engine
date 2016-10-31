package se;

import java.io.IOException;
import java.util.Arrays;

/**
 * Entry point for search server to response to the client with the search result
 */
public class SearchServer {
    public static void main(String[] args) throws IOException {
        System.out.print("Content-type: text/html; charset=utf-8\n\n"); // returning http response texts, let browser knows that I'm printing HTML
        System.out.print("<title>CGI Test from Java</title>\n");

        System.out.println("Received query: " + Arrays.toString(args));
        System.out.println("<hr style=\"width: 100%\" />");

        // To invalidate the KeywordsStorage, and restart crawling at the below lines.
        KeywordsStorage.INSTANCE.invalidate();

        // restart crawling based on results of the HKBU website
        Crawler crawler = new Crawler("http://hkbu.edu.hk/eng/main/index.jsp", 100, 10);
        crawler.start();

        if (args.length == 0 || args[0].length() == 0) {
            System.out.println("No user inputs!");
            return;
        }

        Keyword kw = KeywordsStorage.INSTANCE.get(args[0]);
        if (kw != null)
            kw.printHtml();

        System.out.println("<hr style=\"width: 100%\" />");
    }
}
