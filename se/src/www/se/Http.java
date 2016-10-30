package se;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Demonstrate basic http operations.
 */
public class Http {
    private static final int DEFAULT_TIMEOUT = 30000;

    /**
     * Retrieve html file of given url.
     */
    @Deprecated
    public static String getTextual(URL url) throws IOException {
//        StringBuilder sb = new StringBuilder();
        String s = "";
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(DEFAULT_TIMEOUT);
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

    public static HttpResult get(URL url) throws IOException {
        return get(url, DEFAULT_TIMEOUT);
    }

    public static HttpResult get(URL url, int timeout) throws IOException {

        long start = System.currentTimeMillis();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        long elapsedTime = System.currentTimeMillis() - start;

        conn.setConnectTimeout(timeout);
        File tempFile = new File("tmp/" + UUID.randomUUID().toString() + ".tmp", true);

        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(conn.getInputStream()))) {
            try (OutputStream os = new BufferedOutputStream(tempFile.getOutputStream())) {
                byte[] buffer = new byte[4096];
                int bytesRead = 0;
                while ((bytesRead = dis.read(buffer)) > -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        }

        return new HttpResult(tempFile, elapsedTime, conn);
    }
}
