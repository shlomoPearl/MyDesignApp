//package com.example.mydesign;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.squareup.picasso.Downloader;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.Map;
//
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//public class OpenAi extends AppCompatActivity {
//    final String BASE_URL = "https://api.openai.com/v1/";
//    final String TOKEN = "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";
//    final String GENERATE_IMAGE = "images/generations";
//    final String SIZE_256 = "256x256";
//    final String SIZE_512 = "512x512";
//    final String SIZE_1024 = "1024x1024";
//    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // Create a RequestQueue
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//// Create the request body as a JSON object
//        JSONObject requestBody = new JSONObject();
//        try {
//            requestBody.put("input_text", "cat talk in phone");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            requestBody.put("model", "image-alpha-001");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//// Create the POST request
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                "https://api.openai.com/v1/images/generations",
//                response -> {
//                    // Parse the response here
//                },
//                error -> {
//                    // Handle errors here
//                }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//
//            @Override
//            public byte[] getBody() {
//                return requestBody.toString().getBytes(StandardCharsets.UTF_8);
//            }
//
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + TOKEN);
//                return headers;
//            }
//        };
//
//// Add the request to the RequestQueue
//        queue.add(request);
//
//
////        OkHttpClient client = new OkHttpClient();
////
////        // Set up request body with input text or image
////        MediaType mediaType = MediaType.parse("application/json");
////        RequestBody body = RequestBody.create(mediaType, "{\"input\": \"cat with phone\"}");
////
////        // Set up request with POST method, API endpoint URL, and request body
////        Request request = new Request.Builder()
////                .url("https://api.openai.com/v1/images/generations")
////                .post(body)
////                .addHeader("Authorization", TOKEN)
////                .build();
////
////        // Execute request and handle response
////        Response response = null;
////        try {
////            response = client.newCall(request).execute();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////
//////    String apiResponse = makeAPICall(apiUrl, apiKey, inputTextOrImage);
////
////        // Parse the JSON response
////        JSONObject jsonResponse = null;
////        try {
////            jsonResponse = new JSONObject(response.body().string());
////        } catch (JSONException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////
////        // Get the generated image from the response
////        String imageUrl = null;
////        try {
////            imageUrl = jsonResponse.getString("imageUrl");
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////        Log.d("TAG", "onCreate: " + imageUrl);
////
////// You can then use this image URL to download the image and display it in your app
////
////
////    }
////    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
////        protected Long doInBackground(URL... urls) {
////            int count = urls.length;
////            long totalSize = 0;
////            for (int i = 0; i < count; i++) {
////                totalSize += Downloader.downloadFile(urls[i]);
////                publishProgress((int) ((i / (float) count) * 100));
////                // Escape early if cancel() is called
////                if (isCancelled()) break;
////            }
////            return totalSize;
////        }
//
////        protected void onProgressUpdate(Integer... progress) {
////            setProgressPercent(progress[0]);
////        }
////
////        protected void onPostExecute(Long result) {
////            showDialog("Downloaded " + result + " bytes");
////        }
//    }
//
//
//}
