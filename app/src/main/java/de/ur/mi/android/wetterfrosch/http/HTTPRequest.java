package de.ur.mi.android.wetterfrosch.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class HTTPRequest {

    private static final String LINE_BREAK_DELIMITER = "\n";

    public static void get(URL url, HTTPRequestListener listener) {
        HTTPRequestTask requestTask = new HTTPRequestTask(url, listener);
        Executors.newSingleThreadExecutor().submit(requestTask);
    }


    private static class HTTPRequestTask implements Runnable {

        private URL url;
        private HTTPRequestListener listener;

        HTTPRequestTask(URL url, HTTPRequestListener listener) {
            this.url = url;
            this.listener = listener;
        }

        @Override
        public void run() {
            try {
                HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                InputStreamReader inputReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader((inputReader));
                String result = bufferedReader.lines().collect(Collectors.joining(LINE_BREAK_DELIMITER));
                inputStream.close();
                connection.disconnect();
                listener.onResult(result);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }
    }
}