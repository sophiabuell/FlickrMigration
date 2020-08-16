package com.company;

import java.util.Scanner;

public class CommonHelperMethods {

    public static String parseIdFromURL(String collectionURL){
        String elements[] = collectionURL.split("/");
        String collectionId = elements[elements.length-1];
        return collectionId;
    }

    /**
     * getUrlInput
     * Prompts user to provide a url for the collection or album
     * Verifies formatting of the URL
     * @return verified url
     */
    public static String getURLInput() {
        Scanner albumUrlPrompt = new Scanner(System.in);
        System.out.println("Please provide a url: ");
        String url = albumUrlPrompt.next();
        if (!validateURL(url)){
            //TODO Album url validation
            System.out.println("Invalid URL.");
        }
        return url;
    }

    private static boolean validateURL(String url) {
        return true;
    }


}
