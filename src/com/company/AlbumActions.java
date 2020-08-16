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
        String userId = null;
        try {
            userId = grabUserId(albumURL);
            String albumId = parseIdFromURL(albumURL);
            JSONArray assets = grabPhotoSetAssets(albumId, userId);
            // TODO: Use photoSize endpoint to get url for photo
            // TODO: Download photo using photo url
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateAlbumUrl(String albumURL) {
        return true;
    }
}
