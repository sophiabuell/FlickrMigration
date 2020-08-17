package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

import static com.company.CommonHelperMethods.parseIdFromURL;
import static com.company.FlickrAPIEndpoints.getCollectionTree;
import static com.company.FlickrAPIEndpoints.lookupUser;

public class FlickrCollection {

    private String collectionId;
    private ArrayList<FlickrAlbum> albums = new ArrayList<>();
    private String DIR_PATH = "metadata";
    private String userId;

    FlickrCollection(String collectionUrl) throws IOException {
        collectionId = parseIdFromURL(collectionUrl);
        userId = lookupUser(collectionUrl);
    }

    public void populate() {
        try {
            JSONObject jsonInfo = new JSONObject(getCollectionTree(collectionId, userId));
            JSONArray collections = jsonInfo.getJSONObject("collections").getJSONArray("collection");

            JSONArray assets = collections.getJSONObject(0).getJSONArray("set");
            System.out.println("Getting photoSet assets...");
            for (int i = 0; i < assets.length(); i++) {
                System.out.println(String.format("    Getting asset info for photoSet %d of %d...", i+1, assets.length()));
                JSONObject asset = assets.getJSONObject(i);
                String photoSetId = asset.getString("id");
                FlickrAlbum album = new FlickrAlbum(photoSetId, userId);
                album.populate();
                albums.add(album);
                System.out.println("DONE.");
            }
            System.out.println("DONE.");
            writeCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeCSV() throws IOException {

        // CREATE AND WRITE FILE
        File metadataFile = new File(String.format("%s/%s_metadata.csv", DIR_PATH, collectionId));
        File metadataFolder = new File(DIR_PATH);

        if (!metadataFolder.exists()) {
            metadataFolder.mkdir();
        }
        if (metadataFile.exists() && !metadataFile.delete()) {
            throw new FileAlreadyExistsException(metadataFile.getName());
        }

        if (metadataFile.createNewFile()) {
            FileWriter writer = new FileWriter("metadata/" + collectionId + "_metadata.csv");

            // Write csv template line
            writer.write("id, created, title, width, height, url \n");

            System.out.println(String.format("Writing assets to file %s...", metadataFile.getName()));
            int assetCount = 1;
            for (FlickrAlbum album : albums) {
                for (PhotoAsset asset : album.getAlbumPhotos()) {
                    // Write data line for each asset
                    System.out.println(String.format("    Writing asset %d...", assetCount));
                    writer.write(asset.printCSVLine());
                    assetCount++;
                }
            }
            System.out.println("DONE.");
            writer.close();
        } else {
            throw new IOException("Cannot create file " + metadataFile + ". CSV not written.");
        }
    }
}
