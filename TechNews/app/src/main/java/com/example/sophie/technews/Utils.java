package com.example.sophie.technews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Sophie on 2/27/2017.
 */

public final class Utils {

    public static final String LOG_TAG = Utils.class.getSimpleName();
    public static final ArrayList<News> newsArray = new ArrayList<>();
    public static final String UNKNOWN_STRING="Unknown";
    private Utils() {
    }

    public static ArrayList<News> extractNews(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        ArrayList<News> newsArray = extractItemsFromJson(jsonResponse);
        return newsArray;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        Log.e(LOG_TAG,"makeHttpRequest");
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<News> extractItemsFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        newsArray.clear();
        try {

            JSONObject response = new JSONObject(newsJSON).getJSONObject("response");
            JSONArray items = response.getJSONArray("results");
            for (int i = 0; i < items.length(); i++) {
                JSONObject ithitem = (JSONObject) items.get(i);
                String date = ithitem.getString("webPublicationDate");
                String title = ithitem.getString("webTitle");
                String url = UNKNOWN_STRING;
                if (ithitem.has("webUrl")) {
                    url = ithitem.getString("webUrl");
                }
                String section = UNKNOWN_STRING;
                if (ithitem.has("sectionName")) {
                    section = ithitem.getString("sectionName");
                }
                String author = UNKNOWN_STRING;
                JSONObject tags;
                if (ithitem.has("tags")) {
                    JSONArray tagsArray = (JSONArray) ithitem.getJSONArray("tags");
                    if (!tagsArray.isNull(0)) {
                        tags = (JSONObject) tagsArray.getJSONObject(0);
                        if (tags.has("webTitle")) {
                            author = tags.getString("webTitle");
                        }
                    }
                    newsArray.add(new News(title, author, date, section, url));
                }
            }
            return newsArray;
        }catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the newsArray JSON results", e);
        }
        return null;
    }

}

