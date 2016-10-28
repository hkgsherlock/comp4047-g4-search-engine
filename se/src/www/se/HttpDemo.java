package se;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Demonstrate basic http operations.
 * <p/>
 * Created by YI Peipei on 8/19/2016.
 */
public class HttpDemo {

    /**
     * Retrieve html file of given url.
     *
     * @param url
     */
    public String getTextual(String url) {
//        StringBuilder sb = new StringBuilder();
        String s = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                s += line + "\r\n";
            }

            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getFirstLine(String url) {
        String s = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            s += line;
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        String url = "http://hkgsherlock.github.io/";

        HttpDemo httpDemo = new HttpDemo();
        String response = httpDemo.getTextual(url);
        System.out.println(response);
    }
}
