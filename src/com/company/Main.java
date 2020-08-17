package com.company;

import java.io.IOException;
import java.util.Scanner;

import static com.company.CommonHelperMethods.getURLInput;

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
                    try {
                        FlickrCollection flickrCollection = new FlickrCollection(getURLInput());
                        flickrCollection.populate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        FlickrAlbum flickrAlbum = new FlickrAlbum(getURLInput());
                        flickrAlbum.populate();
                        flickrAlbum.batchDownloadAlbum();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
