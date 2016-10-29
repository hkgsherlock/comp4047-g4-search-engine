package se;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Crawler is a class to do grabbing of all necessary urls from a url.
 */
public class Crawler {
    Queue<String> urlPool = new LinkedList<String>();
    int Y = -1;
    int X = -1;

    /**
     * To crawl a web page with its &lt;a&gt; URLs with specified recursion level and minimum number of results.
     *
     * @param url The URL of web page to crawl.
     * @param Y   The minimum number of results.
     * @param X   How many recursion to run on crawled URLs from the first web page.
     */
    public Crawler(String url, int Y, int X) {
        urlPool.offer(url);
        this.Y = Y;
        this.X = X;
    }

    public void start() {
        // TODO: move crawling of a url using new function instead of writing on start() directly -- charles

        FileDemo fd = new FileDemo();

        /*
        String firstIn = urlPool.get(0);
        String fileName = firstIn.substring(firstIn.indexOf(".") + 1, firstIn.indexOf(".", firstIn.indexOf(".") + 1));
        String linkString = "";
        Document doc = Jsoup.parse(urlPool.get(0));
        for (int i = 0; i < urlPool.size(); i++) {
            boolean isJsp = urlPool.get(i).substring(urlPool.get(i).length() - 3, urlPool.get(i).length() - 1) == "jsp";
            HttpDemo hd = new HttpDemo();
            String fileText;
            if (!isJsp) {
                fileText = hd.getTextual(urlPool.get(i));
                linkString += "" + (i + 1) + ": " + urlPool.get(i) + "\n"   //adding http link as result
                        + hd.getFirstLine(urlPool.get(i)) + "\n";  //adding first line as a keyword
            } else {
                //fileText=hd.getJsp(urlPool.getTextual(i));
                fileText = hd.getTextual(urlPool.get(i));
            }
            int lastIndex = 0;
            lastIndex = fileText.indexOf("URL");
            System.out.println("The First Last Index: " + lastIndex);
            while (lastIndex != -1) {
                String newLink = fileText.substring(lastIndex + 1, fileText.indexOf("\"", lastIndex + 1));
                System.out.println("The Value Of New Link: " + newLink);
                urlPool.add(newLink);
                lastIndex = fileText.indexOf("\"", lastIndex) - 1;
                System.out.println("The Second Last Index: " + lastIndex);
                lastIndex = fileText.indexOf("Url", lastIndex);
                System.out.println("The Third Last Index: " + lastIndex);
            }
        }
        */

        try {
            URL urlFirstPage = new URL(urlPool.poll());

            String fileName = urlFirstPage.getHost().replace('.', '_'); // www_hkbu_edu_hk

            Document docFirstPage = this.getFirstPage(urlFirstPage);




            fd.write(fileName, linkString);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document getFirstPage(String urlString) throws IOException {
        return this.getFirstPage(new URL(urlString));
    }

    private Document getFirstPage(URL url) throws IOException {
        /* TODO: check if html has meta, and check if it contains
                <META http-equiv="refresh" content="0;URL=http://www.hkbu.edu.hk/eng/main/index.jsp">
            and to get the url={url} by RegEx, sth like:
                String meta_refresh_content = jsoup.query("meta[http-equiv=refresh]").attr("content");
                Regex regex_iri = /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/;
                String meta_refresh_url = regex_iri.match(meta_refresh_content)[0]; // http://stackoverflow.com/questions/161738
            and to check the meta_refresh_url again -- charles
         */

        Document startingPage = this.getPage(url);

        // check if it contains meta:refresh AND with new page url
        Elements eleMetaRefresh = startingPage.select("meta[http-equiv=refresh]");
        if (eleMetaRefresh.size() > 0) {
            String[] contentDatas = startingPage.attr("content").split("; *");
            for (String e : contentDatas) {
                String[] kv = e.split(" *= *");
                String key = kv[0];
                String value = kv[1];
                if (key.toLowerCase().equals("url")) {
                    return getFirstPage(url);
                }
            }
        }

        return startingPage;
    }

    private Document getPage(String urlString) throws IOException {
        return this.getPage(new URL(urlString));
    }

    private Document getPage(URL url) throws IOException {
        return Jsoup.parse(url, 10000);
    }

    private URL[] fetchUrlsInAElementsFromDocument(Document doc) {
        return fetchUrlsInAElementsFromDocument(doc, -1);
    }

    private URL[] fetchUrlsInAElementsFromDocument(Document doc, int maxResults) {
        ArrayList<URL> urls = new ArrayList<URL>();

        Elements a = doc.select("a[href]");

        int fetchedCount = 0;
        for (Element ea : a) {
            // "abs:" means not to fetch anchor urls or javascript links etc
            String url_extracted = a.attr("abs:href");

            // if no extraction then pass
            if (url_extracted.length() == 0)
                continue;

            // putting url
            try {
                urls.add(new URL(url_extracted));
                fetchedCount++;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (fetchedCount > -1 && fetchedCount > maxResults)
                return (URL[]) urls.toArray();
        }

        return (URL[]) urls.toArray();
    }

    private void fetchTextualDataAsKeywords(Document doc) {

    }

    private String fetchMetaKeywordContent(Document doc) {
        return doc.select("meta[keywords]").attr("content");
    }

    private String[] fetchPText(Document doc) {
        ArrayList<String> texts = new ArrayList<String>();
        Elements p = doc.select("p");
        for (Element ep : p) {
            String text = ep.text();
            if (text.length() > 0)
                continue;
            texts.add(text);
        }
        return (String[]) texts.toArray();
    }

    /**
     * test entry point for craeling http://www.hkbu.edu.hk
     */
    public static void main(String[] args) {
        // force test code
        Crawler craw = new Crawler("http://www.hkbu.edu.hk", 100, 10);
        craw.start();
        /**/
        if (0 == args.length) {
            System.exit(-1);
        }

        String start_url = null;
        int Y = -1;
        int X = -1;
        if (1 <= args.length) {
            start_url = args[0];
        }
        if (2 <= args.length) {
            Y = Integer.parseInt(args[1]);
        }
        if (3 <= args.length) {
            X = Integer.parseInt(args[2]);
        }
        System.out.println("start crawling from " + start_url + " with Y=" + Y + " and X=" + X);
        Crawler crawler = new Crawler(start_url, Y, X);
        crawler.start();
    }
}