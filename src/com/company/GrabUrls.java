package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GrabUrls {
    static final String API_KEY = "ee684ec62c0fa830c6ce9d0847f50613";
    static final String baseURL = " https://www.flickr.com/services/rest/";

    private static String getResponse(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }

    public static String grabUserId(String collectionURL) throws IOException {
        URL url = new URL(String.format("%s?method=flickr.urls.lookupUser&api_key=%s&url=%s&format=json",
                baseURL, API_KEY, collectionURL));
        String response = getResponse(url);

        // Parse out userID from response
        response = response.split(":")[2];
        String user = response.split(",")[0];
        user = user.substring(1, user.length()-1);
        return user;
    }

    public static String grabCollectionMetadata(String collectionURL, String collectionId, String userId) throws IOException {
        URL url = new URL(String.format("%s?method=flickr.collections.getTree&api_key=%s&collection_id=%s&user_id=%s&format=json",
                baseURL, API_KEY, collectionId, userId));

        String content = getResponse(url);
        String jsonString = content.substring(14, content.length()-1);
        return jsonString;
    }

    public static ArrayList<JSONObject> grabPhotoSetAssets(String photoSetId, String userId) throws IOException {
        URL url = new URL(String.format("%s?method=flickr.photosets.getPhotos&api_key=%s&photoset_id=%s&user_id=%s&format=json", baseURL, API_KEY, photoSetId, userId));
        String response = getResponse(url);
        response = response.substring(14, response.length()-1);
        JSONObject photoSet = new JSONObject(response);
        JSONArray photos = photoSet.getJSONObject("photoset").getJSONArray("photo");
        ArrayList<JSONObject> assets = new ArrayList<>();
        for (int i = 0; i < photos.length(); i++) {
            String id = photos.getJSONObject(i).getString("id");
            assets.add(grabPhotoInfo(id));
        }
        return assets;
    }

    private static JSONObject grabPhotoInfo(String id) throws IOException {
        URL url = new URL(String.format("%s?method=flickr.photos.getInfo&api_key=%s&photo_id=%s&format=json", baseURL, API_KEY, id));
        String response = getResponse(url);

        String jsonString = response.substring(14, response.length()-1);
        System.out.println(jsonString);
        return new JSONObject(jsonString);
    }



    public static String grabSize(String id) throws IOException {
        URL url = new URL(String.format("%s?method=flickr.photos.getSizes&api_key=%s&photo_id=%s&format=json", baseURL, API_KEY, id));
        return getResponse(url);
    }
}
