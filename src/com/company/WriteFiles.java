package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static com.company.GrabUrls.grabPhotoSize;

public class WriteFiles {
    public static String formatPhotoCSVLine(JSONObject asset) throws IOException {
        JSONObject photo = asset.getJSONObject("photo");

        // Get Height and Width
        String response = grabPhotoSize(photo.getString("id"));
        String jsonString = response.substring(14, response.length()-1);
        JSONArray photoSizes = new JSONObject(jsonString).getJSONObject("sizes").getJSONArray("size");
        JSONObject size = photoSizes.getJSONObject(photoSizes.length()-1);

        // Set Values
        String id = photo.getString("id");
        String created = photo.getJSONObject("dates").getString("posted");
        String title = photo.getJSONObject("title").getString("_content").replaceAll(",", "");
        String photoUrl = photo.getJSONObject("urls").getJSONArray("url").getJSONObject(0).getString("_content");
        int width = size.getInt("width");
        int height = size.getInt("height");

        return String.format("%s, %s, %s, %d, %d, %s \n", id, created, title, width, height, photoUrl);
    }

    public static void writeAssets(String collectionId, ArrayList<JSONObject> assets) throws IOException {

        // CREATE AND WRITE FILE
        File metadataFile = new File("metadata/" + collectionId + "_metadata.csv");
        File metadataFolder = new File("metadata");

        if (!metadataFolder.exists()) {
            metadataFolder.mkdir();
        }

        metadataFile.createNewFile();
        FileWriter writer = new FileWriter("metadata/"+collectionId+"_metadata.csv");

        // Write csv template line
        writer.write("id, created, title, width, height, url \n");

        // Write data line for each asset
        System.out.println(String.format("Writing assets to file %s...", metadataFile.getName()));
        int assetCount = 1;
        for (JSONObject asset : assets) {
            System.out.println(String.format("    Writing asset %d of %d...", assetCount, assets.size()));
            writer.write(formatPhotoCSVLine(asset));
            assetCount++;
        }
        System.out.println("DONE.");
        writer.close();
    }
}
