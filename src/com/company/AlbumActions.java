package com.company;

import org.json.JSONArray;

import java.io.IOException;
import java.util.Scanner;

import static com.company.CommonHelperMethods.parseIdFromURL;
import static com.company.GrabUrls.grabPhotoSetAssets;
import static com.company.GrabUrls.grabUserId;

public class AlbumActions {

    public void batchDownloadAlbum() {
        Scanner albumUrlPrompt = new Scanner(System.in);
        System.out.println("Please provide a album url: ");
        String albumURL = albumUrlPrompt.next();
        if (!validateAlbumUrl(albumURL)){
            //TODO Album url validation
            System.out.println("Invalid album URL.");
            return;
        }
        try {
            String userId = grabUserId(albumURL);
            String albumId = parseIdFromURL(albumURL);
            JSONArray assets = grabPhotoSetAssets(albumId, userId);
            System.out.println("Getting photos for album...");
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
