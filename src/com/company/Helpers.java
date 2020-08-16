package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.company.GrabUrls.grabPhotoSetAssets;

public class Helpers {
    public static String parseCollectionURL(String collectionURL){
        String elements[] = collectionURL.split("/");
        String collectionName = elements[elements.length-1];
        return collectionName;
    }

    public static ArrayList<JSONObject> grabAssets(String collectionjson, String userId) throws IOException {
        JSONObject info = new JSONObject(collectionjson);
        JSONArray collections = info.getJSONObject("collections").getJSONArray("collection");

        JSONArray assets = collections.getJSONObject(0).getJSONArray("set");
        ArrayList<JSONObject> assetsInfo = new ArrayList();
        for (int i = 0; i < assets.length(); i++) {
            JSONObject asset = assets.getJSONObject(i);
            String photoSetId = asset.getString("id");

            assetsInfo.addAll(grabPhotoSetAssets(photoSetId, userId));
        }
        return assetsInfo;
    }
}
