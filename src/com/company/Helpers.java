package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.company.GrabUrls.grabPhotoSetAssets;

public class Helpers {
    public static String parseCollectionURL(String collectionURL){
        String elements[] = collectionURL.split("/");
        String collectionId = elements[elements.length-1];
        return collectionId;
    }

    public static ArrayList<JSONObject> grabAssets(String collectionJson, String userId) throws IOException {
        JSONObject info = new JSONObject(collectionJson);
        JSONArray collections = info.getJSONObject("collections").getJSONArray("collection");

        JSONArray assets = collections.getJSONObject(0).getJSONArray("set");
        ArrayList<JSONObject> assetsInfo = new ArrayList();
        System.out.println("Getting photoSet assets...");
        for (int i = 0; i < assets.length(); i++) {
            JSONObject asset = assets.getJSONObject(i);
            String photoSetId = asset.getString("id");

            System.out.println(String.format("    Getting asset info for photoSet %d of %d...", i+1, assets.length()));
            assetsInfo.addAll(grabPhotoSetAssets(photoSetId, userId));
        }
        System.out.println("DONE.");
        return assetsInfo;
    }
}
