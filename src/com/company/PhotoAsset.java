package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static com.company.FlickrAPIEndpoints.getPhotoInfo;
import static com.company.FlickrAPIEndpoints.getPhotoSizes;

public class PhotoAsset {
    private String url;
    private int height;
    private int width;
    private String id;
    private String title;
    private String created;

    PhotoAsset(String id) throws IOException {
        JSONObject photoObject = getPhotoInfo(id).getJSONObject("photo");
        JSONObject largestPhotoObject =  getLargestPhotoSizeObject(getPhotoSizes(id));
        this.id = id;
        this.created = photoObject.getJSONObject("dates").getString("posted");
        this.title = photoObject.getJSONObject("title").getString("_content").replaceAll(",", "");
        this.url = largestPhotoObject.getString("source");
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

    public boolean download() throws IOException {
        InputStream input = new URL(url).openStream();
        String ext = url.substring(url.length()-4);
        File imageFile = new File("media/"+id+"."+ext);
        if (imageFile.createNewFile()) {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            outputStream.write(input.readAllBytes());
            outputStream.close();
            System.out.println("DONE.");
        } else {
            System.out.println("Error creating file: " + imageFile.getName());
        }
        return true;
    }
}
