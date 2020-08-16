package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static com.company.CommonHelperMethods.parseIdFromURL;
import static com.company.GrabUrls.grabPhotoSetAssets;
import static com.company.GrabUrls.grabPhotoInfo;
import static com.company.GrabUrls.grabCollectionMetadata;
import static com.company.GrabUrls.grabUserId;

public class CollectionActions {

    private WriteFiles fileWriter = new WriteFiles();

    public void extractCollectionInfo() {
        Scanner collectionURLPrompt = new Scanner(System.in);
        System.out.println("Please provide a collection url: ");
        String collectionURL = collectionURLPrompt.next();

        if (!validateCollectionURL(collectionURL)){
            // TODO Collection url validation
            System.out.println("Invalid collection URL.");
            return;
        }

        try {
            String userId = grabUserId(collectionURL);
            String collectionId = parseIdFromURL(collectionURL);
            String jsonInfo = grabCollectionMetadata(collectionURL, collectionId, userId);

            ArrayList<JSONObject> assets = extractAssetInfo(jsonInfo, userId);
            fileWriter.writeAssets(collectionId, assets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateCollectionURL(String collectionURL) {
        return true;
    }

    private ArrayList<JSONObject> extractAssetInfo(String collectionJson, String userId) throws IOException {
        JSONObject info = new JSONObject(collectionJson);
        JSONArray collections = info.getJSONObject("collections").getJSONArray("collection");

        JSONArray assets = collections.getJSONObject(0).getJSONArray("set");
        ArrayList<JSONObject> assetsInfo = new ArrayList();
        System.out.println("Getting photoSet assets...");
        for (int i = 0; i < assets.length(); i++) {
            JSONObject asset = assets.getJSONObject(i);
            String photoSetId = asset.getString("id");

            System.out.println(String.format("    Getting asset info for photoSet %d of %d...", i+1, assets.length()));
            JSONArray photos = grabPhotoSetAssets(photoSetId, userId);

            ArrayList<JSONObject> albumAssets = new ArrayList<>();
            for (int j = 0; j < photos.length(); j++) {
                String id = photos.getJSONObject(i).getString("id");
                albumAssets.add(grabPhotoInfo(id));
            }
            assetsInfo.addAll(albumAssets);
        }
        System.out.println("DONE.");
        return assetsInfo;
    }
}
