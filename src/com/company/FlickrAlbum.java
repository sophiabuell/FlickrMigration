package com.company;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static com.company.CommonHelperMethods.getURLInput;
import static com.company.CommonHelperMethods.parseIdFromURL;
import static com.company.FlickrAPIEndpoints.listAlbumPhotos;
import static com.company.FlickrAPIEndpoints.lookupUser;

public class FlickrAlbum {

    public void batchDownloadAlbum() {
        String albumURL = getURLInput();
        try {
            String userId = lookupUser(albumURL);
            String albumId = parseIdFromURL(albumURL);
            JSONArray assets = listAlbumPhotos(albumId, userId);
            System.out.println("Getting photos for album...");
            File mediaFile = new File("media");
            if (!mediaFile.exists()) {
                mediaFile.mkdir();
            }

            for (int i = 0; i < assets.length(); i++) {
                System.out.print(String.format("Getting info for photo %d of %d... ", i, assets.length()));
                String id = assets.getJSONObject(i).getString("id");
                PhotoAsset photoAsset = new PhotoAsset(id);
                System.out.print("Downloading...");
                photoAsset.download();
                System.out.print("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateAlbumUrl(String albumURL) {
        return true;
    }
}
