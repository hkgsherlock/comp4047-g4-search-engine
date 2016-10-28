package se;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;

import java.util.LinkedList;


public class Crawler {
    LinkedList<String> urlPool = new LinkedList<String>();
    int Y = -1;
    int X = -1;

    public Crawler(String url, int Y, int X) {
        urlPool.add(url);
        this.Y = Y;
        this.X = X;
    }

    public void start() {
        /* TODO: check if html has meta, and check if it contains
            <META http-equiv="refresh" content="0;URL=http://www.hkbu.edu.hk/eng/main/index.jsp">
            and to get the url={url} by RegEx, sth like:
                String meta_refresh_content = jsoup.query("meta[http-equiv=refresh]").attr("content");
                Regex regex_iri = /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/;
                String meta_refresh_url = regex_iri.match(meta_refresh_content)[0]; // http://stackoverflow.com/questions/161738
            and to check the meta_refresh_url again -- charles
         */
        // TODO: move crawling of a url using new function instead of writing on start() directly -- charles

        FileDemo fd = new FileDemo();
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
        fd.write(fileName, linkString);
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