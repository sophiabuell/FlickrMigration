package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Main {
    static final String API_KEY = "ee684ec62c0fa830c6ce9d0847f50613";
    static final String baseURL = " https://www.flickr.com/services/rest/";


    public static String grapUserId(String collectionURL) throws IOException {
        URL url = new URL(String.format("%s?method=flickr.urls.lookupUser&api_key=%s&url=%s&format=json",
                baseURL, API_KEY, collectionURL));
        String response = getStringBuffer(url);

        // Parse out userID from response
        response = response.split(":")[2];
        String user = response.split(",")[0];
        user = user.substring(1, user.length()-1);
        return user;
    }

    public static String parseCollectionURL(String collectionURL){
        String elements[] = collectionURL.split("/");
        String collectionName = elements[elements.length-1];
        return collectionName;
    }

    public static String exportCollectionMetadata(String collectionURL, String collectionId, String userId) throws IOException {
        URL url = new URL(String.format("%s?method=flickr.collections.getTree&api_key=%s&collection_id=%s&user_id=%s&format=json",
                baseURL, API_KEY, collectionId, userId));

        String content = getStringBuffer(url);
        String jsonString = content.substring(14, content.length()-1);
        return jsonString;
    }

    private static String getStringBuffer(URL url) throws IOException {
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


    public static ArrayList<JSONObject> grabAssets(String collectionjson, String userId) throws IOException {
        JSONObject info = new JSONObject(collectionjson);
        JSONArray collections = info.getJSONObject("collections").getJSONArray("collection");

        String collectionId = collections.getJSONObject(0).getString("id");
        JSONArray assets = collections.getJSONObject(0).getJSONArray("set");
        ArrayList<JSONObject> assetsInfo = new ArrayList();
        for (int i = 0; i < assets.length(); i++) {
            JSONObject asset = assets.getJSONObject(i);
            String photoSetId = asset.getString("id");

            assetsInfo.addAll(grabPhotoSetAssets(photoSetId, userId));
        }
        return assetsInfo;
    }

    private static ArrayList<JSONObject> grabPhotoSetAssets(String photoSetId, String userId) throws IOException {
        URL url = new URL(String.format("%s?method=flickr.photosets.getPhotos&api_key=%s&photoset_id=%s&user_id=%s&format=json", baseURL, API_KEY, photoSetId, userId));
        String response = getStringBuffer(url);
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
        String response = getStringBuffer(url);

        String jsonString = response.substring(14, response.length()-1);
        System.out.println(jsonString);
        return new JSONObject(jsonString);
    }

    public static String formatPhotoCSVLine(JSONObject asset) throws IOException {
        JSONObject photo = asset.getJSONObject("photo");

        String id = photo.getString("id");
        String created = photo.getJSONObject("dates").getString("posted");
        String title = photo.getJSONObject("title").getString("_content").replaceAll(",", "");
        String photoUrl = photo.getJSONObject("urls").getJSONArray("url").getJSONObject(0).getString("_content");

        // Get Height and Width
        URL url = new URL(String.format("%s?method=flickr.photos.getSizes&api_key=%s&photo_id=%s&format=json", baseURL, API_KEY, id));
        String response = getStringBuffer(url);
        String jsonString = response.substring(14, response.length()-1);
        JSONArray photoSizes = new JSONObject(jsonString).getJSONObject("sizes").getJSONArray("size");
        JSONObject size = photoSizes.getJSONObject(photoSizes.length()-1);
        int width = size.getInt("width");
        int height = size.getInt("height");
        return String.format("%s, %s, %s, %d, %d, %s \n", id, created, title, width, height, photoUrl);
    }
    public static void writeAssets(String collectionId, ArrayList<JSONObject> assets) throws IOException {

        // CREATE AND WRITE FILE
        File metadataFile = new File("metadata/" + collectionId + "_metadata.csv");
        File metadataFolder = new File("metadata");
        if (!metadataFolder.exists()) {
            System.out.println("creatingDirectory" + metadataFolder.getAbsolutePath());
            metadataFolder.mkdir();
        }
        metadataFile.createNewFile();
        System.out.println("created file" + metadataFile.getAbsolutePath());
        FileWriter writer = new FileWriter("metadata/"+collectionId+"_metadata.csv");
        writer.write("id, created, title, width, height, url \n");
        for (JSONObject asset : assets) {
            writer.write(formatPhotoCSVLine(asset));
        }
        writer.close();
    }
    public static void main(String[] args) {
	// write your code here
        String collectionURL = args[0];
        try {
            String userId = grapUserId(collectionURL);
            String collectionId = parseCollectionURL(collectionURL);
            String jsonInfo = exportCollectionMetadata(collectionURL, collectionId, userId);

            ArrayList<JSONObject> assets = grabAssets(jsonInfo, userId);
            writeAssets(collectionId, assets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
