package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static com.company.GrabUrls.grabPhotoInfo;
import static com.company.GrabUrls.grabPhotoSize;

public class PhotoAsset {
    private String url;
    private int height;
    private int width;
    private String id;
    private String title;
    private String created;

    PhotoAsset(String id) throws IOException {
        JSONObject photoObject = grabPhotoInfo(id).getJSONObject("photo");
        JSONObject largestPhotoObject =  getLargestPhotoSizeObject(grabPhotoSize(id));
        this.id = id;
        this.created = photoObject.getJSONObject("dates").getString("posted");
        this.title = photoObject.getJSONObject("title").getString("_content").replaceAll(",", "");
        this.url = largestPhotoObject.getString("url");
        this.height = largestPhotoObject.getInt("height");
        this.width = largestPhotoObject.getInt("width");
    }

    public static JSONObject getLargestPhotoSizeObject(JSONObject photoSizeObject){
        JSONArray sizes = photoSizeObject.getJSONObject("sizes").getJSONArray("size");
        return sizes.getJSONObject(sizes.length()-1);
    }

    public String printCSVLine() {
        return String.format("%s, %s, %s, %d, %d, %s \n", id, created, title, width, height, url);
    }

    public boolean download() {
        // TODO
        return true;
    }
}
