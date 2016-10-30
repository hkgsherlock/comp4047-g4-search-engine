package seTest;

import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
import de.l3s.boilerpipe.sax.HTMLFetcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;
import se.Http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class testBoilerpipe {
    public static void main(String[] args) throws Exception {
        doParseTest("http://hkbuenews.hkbu.edu.hk/?t=press_release_details/1580");
        doParseTest("http://www.hkbu.edu.hk/eng/main/index.jsp");
        doParseTest("http://time.com/");
        doParseTest("http://time.com/4549527/hillary-clinton-emails-fbi-reopen/");
        doParseTest("http://time.com/4548320/j-k-rowling-seattle-seahawks-richard-sherman/");
        doParseTest("https://hk.news.yahoo.com/");
        doParseTest("https://hk.news.yahoo.com/%E9%A0%98%E5%B1%95%E6%A8%99%E5%94%AE5%E5%95%86%E5%A0%B4-%E4%BC%B0%E5%80%BC27%E5%84%84-223108942.html");
        doParseTest("http://headlines.yahoo.co.jp/hl?a=20161029-00000002-jct-soci");
        doParseTest("http://yipeipei.github.io/comp4047/");
        doParseTest("http://stackoverflow.com");
        doParseTest("http://monkeycoding.com/?tag=contourarea");
        doParseTest("http://www.pyimagesearch.com/2015/12/21/increasing-webcam-fps-with-python-and-opencv/");
        doParseTest("http://www.pyimagesearch.com/");
    }

    private static void doParseTest(String urlString) throws Exception {
        System.out.println("URL: " + urlString);

        URL url = new URL(urlString);

        long start = System.currentTimeMillis();
        String html = Http.getTextual(url);
        long elapsedTime = System.currentTimeMillis() - start;

        Document doc = Jsoup.parse(html);

        String txtJsoup = doc.select("body").text();

        InputSource is = new InputSource(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)));
        BoilerpipeSAXInput in = new BoilerpipeSAXInput(is);
        TextDocument doc2 = in.getTextDocument();
        String txtBoilerpipe = ArticleExtractor.INSTANCE.getText(doc2);

        System.out.println("Jsoup:");
        System.out.println(txtJsoup);

        System.out.println();
        System.out.println();

        System.out.println("Boilerpipe:");
        System.out.println(txtBoilerpipe);
        System.out.println();

        System.out.println();
        // reduce ranking if page load time is large
        System.out.println("time load page: " + elapsedTime + "ms");

        // if has article tag then consider more using boilerpipe
        System.out.println("has article tag? " + (doc.select("article").size() > 0 ? "YES" : "noooooooooooo"));

        // if has article tag then consider even more using boilerpipe
        System.out.println("has section tag? " + (doc.select("section").size() > 0 ? "YES" : "noooooooooooo"));

        // if percentage is small like < 5%, consider more using all text
        System.out.println("percentage of likeliness: " + ((double)txtBoilerpipe.length() / (double)txtJsoup.length() * 100.0) + "%");

        if (
                (doc.select("article").size() > 0 || doc.select("section").size() > 0) &&
                        ((double)txtBoilerpipe.length() / (double)txtJsoup.length() * 100.0) > 10.0) {
            System.out.println("use boilerpipe");
        } else {
            System.out.println("use jsoup");
        }

        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println();
    }
}
