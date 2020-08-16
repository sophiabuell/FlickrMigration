package com.company;

import java.util.Scanner;

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
                    FlickrCollection flickrCollection = new FlickrCollection();
                    flickrCollection.extractCollectionInfo();
                    break;
                case 1:
                    FlickrAlbum flickrAlbum = new FlickrAlbum();
                    flickrAlbum.batchDownloadAlbum();
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





}
