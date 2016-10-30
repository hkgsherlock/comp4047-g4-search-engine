package se;

import java.io.IOException;
import java.util.Arrays;

/**
 * CGI Test
 * <p/>
 * Created by YI Peipei on 8/19/2016.
 */
public class SearchServer {
    public static void main(String[] args) throws IOException{
        System.out.print("Content-type: text/html\n\n");
        System.out.print("<title>CGI Test from Java</title>\n");
        System.out.print("<p>Hello World!</p>\n");
        System.out.print("Received query: " + Arrays.toString(args));
        System.out.print("testing");
        Crawler crawler = new Crawler("http://hkbu.edu.hk/eng/main/index.jsp", 100, 10);
        crawler.start();
        KeywordsStorage.INSTANCE.get("About").print();
    }
}