package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static com.company.GrabUrls.grabSize;

public class WriteFiles {
    public static String formatPhotoCSVLine(JSONObject asset) throws IOException {
        JSONObject photo = asset.getJSONObject("photo");

        String id = photo.getString("id");
        String created = photo.getJSONObject("dates").getString("posted");
        String title = photo.getJSONObject("title").getString("_content").replaceAll(",", "");
        String photoUrl = photo.getJSONObject("urls").getJSONArray("url").getJSONObject(0).getString("_content");

        // Get Height and Width
        String response = grabSize(id);
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
}
