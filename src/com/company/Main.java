package com.company;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static com.company.GrabUrls.grabCollectionMetadata;
import static com.company.GrabUrls.grabUserId;
import static com.company.Helpers.grabAssets;
import static com.company.Helpers.parseCollectionURL;
import static com.company.WriteFiles.writeAssets;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean running = true;
        while(running) {
            System.out.println("Please choose an option: ");
            System.out.println("[0] export collection metadata to CSV");
            System.out.println("[1] batch download assets from photoSet");
            System.out.println("[2] exitProgram");
            int choice = input.nextInt();
            switch (choice) {
                case 0:
                    extractCollectionInfo();
                    break;
                case 1:
                    batchDownloadAlbum();
                    break;
                case 2:
                    System.out.println("Thanks for playing come again soon!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option please chose again...");
                    break;
            }
        }
    }

    private static void batchDownloadAlbum() {
        //TODO Batch Download
    }

    private static void extractCollectionInfo() {
        Scanner collectionURLPrompt = new Scanner(System.in);
        System.out.println("Please provide a collection url: ");
        String collectionURL = collectionURLPrompt.next();

        if (!validateCollectionURL(collectionURL)){
            // TODO Collection url validation
        }

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

    private static boolean validateCollectionURL(String collectionURL) {
        return true;
    }

}
