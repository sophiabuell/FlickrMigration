package com.company;

import java.util.Scanner;

import static com.company.CollectionActions.extractCollectionInfo;

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
        Scanner albumUrlPrompt = new Scanner(System.in);
        System.out.println("Please provide a album url: ");
        String albumURL = albumUrlPrompt.next();
        if (!validateAlbumUrl(albumURL)){
            //TODO Album url validation
            System.out.println("Invalid album URL.");
            return;
        }

        //TODO Batch Download
    }

    private static boolean validateAlbumUrl(String albumURL) {
        return true;
    }



}
