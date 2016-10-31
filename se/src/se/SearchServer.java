package se;

import java.io.IOException;
import java.util.Arrays;

/**
 * Entry point for search server to response to the client with the search result
 */
public class SearchServer {
    public static void main(String[] args) throws IOException{
        // To invalidate the KeywordsStorage, and restart crawling at the below lines.
        KeywordsStorage.INSTANCE.invalidate();

        System.out.print("Content-type: text/html\n\n"); // returning http response texts, let browser knows that I'm printing HTML
        System.out.print("<title>CGI Test from Java</title>\n");
        System.out.print("<p>Hello World!</p>\n");
        System.out.print("Received query: " + Arrays.toString(args));

        // restart crawling based on results of the HKBU website
        System.out.println("Received query: " + Arrays.toString(args));
        Crawler crawler = new Crawler("http://hkbu.edu.hk/eng/main/index.jsp", 100, 10);
        crawler.start();

        if (args.length == 0 || args[0].length() == 0){
            System.out.println("No user inputs!");
            return;
        }

        Keyword kw = KeywordsStorage.INSTANCE.get(args[0]);
        if (kw != null)
            kw.print();
    }
}
