package com.example.mydesign;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


//
public class OpenAi {
    private final String token = "insert your token here";

    private String prompt;
    private String url;
    public OpenAi(String prompt) {
        System.out.println("token is " + token);
        this.prompt = prompt;
        new SendRequestTask().execute(prompt);

    }


    private OnUrlGeneratedListener listener; // default to null

    //
    public void setOnUrlGeneratedListener(OnUrlGeneratedListener listener) {
        this.listener = listener;
    }


    private class SendRequestTask extends AsyncTask<String, Void, String> {
        public String generate(String prompt){
            try {
                // Create the request URL
                URL url = new URL("https://api.openai.com/v1/images/generations");

                // Create the HTTP connection
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                con.setRequestMethod("POST");

                // Set the request headers
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Authorization", String.format("Bearer %s", token));

                // Set the request body
                String payload = String.format("{\"prompt\":\"%s\"," +
                                                    "\"size\":\"256x256\"}", prompt);

                // Send the request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(payload);
                wr.close();

                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Print the response
                char c = '"';
                System.out.println(response);
                String[] url_split = response.toString().split(String.valueOf(c));
                System.out.println(url_split[7]);
                return url_split[7];
            } catch (Exception e) {
                return e.toString();

            }
        }

        @Override
        protected void onPostExecute(String url) {
            if (listener != null) {
                listener.onUrlGenerated(url);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String prompt = params[0];
            String url = generate(prompt);
            return url;
        }
    }
}
