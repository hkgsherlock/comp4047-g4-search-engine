package seTest;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import org.jsoup.Jsoup;
import org.jsoup.*;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class testBoilerpipe {
    public static void main(String[] args) throws IOException, BoilerpipeProcessingException {
        Document doc = Jsoup.parse(new URL("http://hkbuenews.hkbu.edu.hk/?t=press_release_details/1580"), 10000);

        System.out.println("Jsoup:");
        System.out.println(doc.select("body").text());

        System.out.println();
        System.out.println();

        System.out.println("Boilerpipe:");
        System.out.println(ArticleExtractor.INSTANCE.getText(doc.html()));
    }
}
