package se;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Demonstrate basic http operations.
 */
public class Http {
    private static int timeout = 30000; // 30 secs

    /**
     * Retrieve html file of given url.
     */
    public static String getTextual(String urlString) throws IOException {
        return getTextual(new URL(urlString));
    }

    /**
     * Retrieve html file of given url.
     */
    public static String getTextual(URL url) throws IOException {
//        StringBuilder sb = new StringBuilder();
        String s = "";
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(timeout);
        // TODO: seperate to different parts, check if contenttype is text/*
        // throws exception if binary code
        InputStream is = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = br.readLine()) != null) {
            s += line + "\r\n";
        }

        return s;
    }
}
