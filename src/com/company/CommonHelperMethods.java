package com.company;

public class CommonHelperMethods {

    public static String parseIdFromURL(String collectionURL){
        String elements[] = collectionURL.split("/");
        String collectionId = elements[elements.length-1];
        return collectionId;
    }
}