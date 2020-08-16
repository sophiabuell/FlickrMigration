package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.company.GrabUrls.grabCollectionMetadata;
import static com.company.GrabUrls.grabUserId;
import static com.company.Helpers.grabAssets;
import static com.company.Helpers.parseCollectionURL;
import static com.company.WriteFiles.writeAssets;

public class Main {
    static final String API_KEY = "ee684ec62c0fa830c6ce9d0847f50613";
    static final String baseURL = " https://www.flickr.com/services/rest/";




    public static void main(String[] args) {
	// write your code here
        String collectionURL = args[0];
        try {
            String userId = grabUserId(collectionURL);
            String collectionId = parseCollectionURL(collectionURL);
            String jsonInfo = grabCollectionMetadata(collectionURL, collectionId, userId);

            ArrayList<JSONObject> assets = grabAssets(jsonInfo, userId);
            writeAssets(collectionId, assets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
