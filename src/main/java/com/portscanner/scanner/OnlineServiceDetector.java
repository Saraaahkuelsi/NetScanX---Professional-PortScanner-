package com.portscanner.scanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OnlineServiceDetector implements ServiceDetector {

    private static final String API_URL = 
        "https://www.speedguide.net/api/ports.php?port=";

    public String detect(int port) {
        try {
            URL url = new URL(API_URL + port);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return parseService(response.toString());

        } catch (Exception e) {
            return "Unknown";
        }
    }

    private String parseService(String response) {
        try {
            if (response.contains("\"service\":\"")) {
                return response.split("\"service\":\"")[1].split("\"")[0];
            }
        } catch (Exception e) {}
        return "Unknown";
    }
}