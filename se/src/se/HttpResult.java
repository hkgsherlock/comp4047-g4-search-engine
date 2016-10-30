package se;

import java.io.IOException;
import java.net.HttpURLConnection;

public class HttpResult extends File {
    private long timeConnect;
    private HttpURLConnection httpConnection;

    public HttpResult(File tempFile, long timeConnect, HttpURLConnection httpConnection) throws IOException {
        super(tempFile.file, false);
        this.timeConnect = timeConnect;
        this.httpConnection = httpConnection;
    }

    public HttpResult(File tempFile, long timeConnect, HttpURLConnection httpConnection, boolean write) throws IOException {
        super(tempFile.file, write);
        this.timeConnect = timeConnect;
        this.httpConnection = httpConnection;
    }

    public String getContentType() {
        String[] contentType = this.httpConnection.getContentType().split(" *; *");
        for (String ct : contentType) {
            if (ct.matches("^(application|audio|example|image|message|model|multipart|text|video)/[a-zA-Z0-9]+([+.-][a-zA-z0-9]+)*$"))
                return ct;
        }
        return null;
    }

    public long getTimeConnect() {
        return timeConnect;
    }

    public HttpURLConnection getHttpConnection() {
        return httpConnection;
    }
}
