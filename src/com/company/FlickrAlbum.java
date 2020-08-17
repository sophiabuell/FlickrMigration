package com.company;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.company.CommonHelperMethods.parseIdFromURL;
import static com.company.FlickrAPIEndpoints.listAlbumPhotos;
import static com.company.FlickrAPIEndpoints.lookupUser;

public class FlickrAlbum {
    ArrayList<PhotoAsset> albumPhotos = new ArrayList<>();
    String userId;
    String albumId;

    FlickrAlbum(String albumURl) throws IOException {
        userId = lookupUser(albumURl);
        albumId = parseIdFromURL(albumURl);
    }

    FlickrAlbum(String albumId, String userId) {
        this.userId = userId;
        this.albumId = albumId;
    }

    public void populate() throws IOException {
        JSONArray assets = listAlbumPhotos(albumId, userId);
        for (int i = 0; i < assets.length(); i++) {
            System.out.print(String.format("Getting info for photo %d of %d... ", i, assets.length()));
            String id = assets.getJSONObject(i).getString("id");
            albumPhotos.add(new PhotoAsset(id));
            System.out.println("DONE.");
        }
    }
    public void batchDownloadAlbum() {
        try {
            File mediaFile = new File("media");
            if (!mediaFile.exists()) {
                mediaFile.mkdir();
            }
            int count = 1;
            for (PhotoAsset photoAsset : albumPhotos){
                System.out.print(String.format("Downloading photo %d of %d...", count, albumPhotos.size()));
                photoAsset.download();
                System.out.println("DONE.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<PhotoAsset> getAlbumPhotos() {
        return albumPhotos;
    }

}
