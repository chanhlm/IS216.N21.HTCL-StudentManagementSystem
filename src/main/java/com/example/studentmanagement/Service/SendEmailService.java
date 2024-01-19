package com.example.studentmanagement.Service;

import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendEmailService {
    public final static Boolean sendEmailNotification(String email, String subject, String body, ObservableList<String> cc) {
        try {
            String url = "http://127.0.0.1:5000/api/v1/email/send-email-service";
            String jsonInputString = "{\"receivers\": [\"" + email + "\"], \"subject\": \"" + subject + "\", \"body\": \"" + body + "\", \"cc\": ["
                    + cc.stream().map(s -> "\"" + s + "\"").reduce((s1, s2) -> s1 + "," + s2).orElse("") + "]}";
            System.out.println(jsonInputString);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Get the response
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            if (responseCode != 200) {
                System.out.println("Error: " + response.toString());
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
