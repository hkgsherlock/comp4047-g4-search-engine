package se;

import org.jsoup.Jsoup;

import java.io.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import java.util.LinkedList;

/**
 * Created by YI Peipei on 8/19/2016.
 */
public class Crawler {
    LinkedList<String> urlPool = new LinkedList<>();
    int Y = -1;
    int X = -1;

    public Crawler(String url, int Y, int X) {
        urlPool.add(url);
        this.Y = Y;
        this.X = X;
    }

    public void start() {
        FileDemo fd=new FileDemo();
        String firstIn=urlPool.get(0);
        String fileName=firstIn.substring(firstIn.indexOf(".")+1,firstIn.indexOf(".",firstIn.indexOf(".")+1));;
        String linkString="";
        Document doc= Jsoup.parse(urlPool.get(0));
        for(int i=0;i<urlPool.size();i++){
            boolean isJsp=urlPool.get(i).substring(urlPool.get(i).length()-3,urlPool.get(i).length()-1)=="jsp";
            HttpDemo hd=new HttpDemo();
            String fileText;
            if(!isJsp) {
                fileText= hd.get(urlPool.get(i));
                linkString += "" + (i + 1) + ": " + urlPool.get(i) + "\n"   //adding http link as result
                        + hd.getFirstLine(urlPool.get(i)) + "\n";  //adding first line as a keyword
            }else{
                //fileText=hd.getJsp(urlPool.get(i));
                fileText= hd.get(urlPool.get(i));
            }
            int lastIndex=0;
            lastIndex=fileText.indexOf("URL");
            System.out.println("The First Last Index: "+lastIndex);
            while(lastIndex!=-1){
                String newLink=fileText.substring(lastIndex+1,fileText.indexOf("\"",lastIndex+1));
                System.out.println("The Value Of New Link: "+newLink);
                urlPool.add(newLink);
                lastIndex=fileText.indexOf("\"",lastIndex)-1;
                System.out.println("The Second Last Index: "+lastIndex);
                lastIndex=fileText.indexOf("Url",lastIndex);
                System.out.println("The Third Last Index: "+lastIndex);
            }
        }
        fd.write(fileName,linkString);
    }

    public static void main(String[] args) {
        // force test code
        Crawler craw=new Crawler("http://www.hkbu.edu.hk",100,10);
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