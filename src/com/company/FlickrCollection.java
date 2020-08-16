package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static com.company.CommonHelperMethods.getURLInput;
import static com.company.CommonHelperMethods.parseIdFromURL;
import static com.company.FlickrAPIEndpoints.listAlbumPhotos;
import static com.company.FlickrAPIEndpoints.getCollectionTree;
import static com.company.FlickrAPIEndpoints.lookupUser;

public class FlickrCollection {

    public void extractCollectionInfo() {
        String collectionURL = getURLInput();

        try {
            String userId = lookupUser(collectionURL);
            String collectionId = parseIdFromURL(collectionURL);
            String jsonInfo = getCollectionTree(collectionURL, collectionId, userId);

            ArrayList<PhotoAsset> assets = extractAssetInfo(jsonInfo, userId);
            writeCSV(collectionId, assets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<PhotoAsset> extractAssetInfo(String collectionJson, String userId) throws IOException {
        JSONObject info = new JSONObject(collectionJson);
        JSONArray collections = info.getJSONObject("collections").getJSONArray("collection");

        JSONArray assets = collections.getJSONObject(0).getJSONArray("set");
        ArrayList<PhotoAsset> assetsInfo = new ArrayList();
        System.out.println("Getting photoSet assets...");
        for (int i = 0; i < assets.length(); i++) {
            JSONObject asset = assets.getJSONObject(i);
            String photoSetId = asset.getString("id");

            System.out.println(String.format("    Getting asset info for photoSet %d of %d...", i+1, assets.length()));
            JSONArray photos = listAlbumPhotos(photoSetId, userId);

            ArrayList<PhotoAsset> albumAssets = new ArrayList<>();
            for (int j = 0; j < photos.length(); j++) {
                String id = photos.getJSONObject(i).getString("id");
                albumAssets.add(new PhotoAsset(id));
            }
            assetsInfo.addAll(albumAssets);
        }
        System.out.println("DONE.");
        return assetsInfo;
    }

    public void writeCSV(String collectionId, ArrayList<PhotoAsset> assets) throws IOException {

        // CREATE AND WRITE FILE
        File metadataFile = new File("metadata/" + collectionId + "_metadata.csv");
        File metadataFolder = new File("metadata");

        if (!metadataFolder.exists()) {
            metadataFolder.mkdir();
        }

        if (metadataFile.createNewFile()) {
            FileWriter writer = new FileWriter("metadata/" + collectionId + "_metadata.csv");

            // Write csv template line
            writer.write("id, created, title, width, height, url \n");

            System.out.println(String.format("Writing assets to file %s...", metadataFile.getName()));
            int assetCount = 1;

            for (PhotoAsset asset : assets) {
                // Write data line for each asset
                System.out.println(String.format("    Writing asset %d of %d...", assetCount, assets.size()));
                writer.write(asset.printCSVLine());
                assetCount++;
            }
            System.out.println("DONE.");
            writer.close();
        } else {
            System.out.println("Error creating file " + metadataFile.getName());
        }
    }
}
