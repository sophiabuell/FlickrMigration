package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FlickrAPIEndpoints {

    // TODO: Write method to format regex for url string generically

    private static final String API_KEY_VALUE = "ee684ec62c0fa830c6ce9d0847f50613";
    private static final String BASE_URL = "https://www.flickr.com/services/rest/";

    // API Methods
    private static final String PHOTOS_GET_SIZES = "flickr.photos.getSizes";
    private static final String FLICKR_URLS_LOOKUP_USER = "flickr.urls.lookupUser";
    private static final String COLLECTIONS_GET_TREE = "flickr.collections.getTree";
    private static final String GET_PHOTOS = "flickr.photosets.getPhotos";
    private static final String PHOTOS_GET_INFO = "flickr.photos.getInfo";

    // URL Query Params
    private static final String METHOD_PARAM = "method";
    private static final String API_KEY_PARAM = "api_key";
    private static final String URL_PARAM = "url";
    private static final String COLLECTION_ID_PARAM = "collection_id";
    private static final String USER_ID_PARAM = "user_id";
    private static final String PHOTOSET_ID_PARAM = "photoset_id";
    private static final String PHOTO_ID_PARAM = "photo_id";

    // JSON format
    private static final String FORMAT_JSON = "format=json";

    private static String getAndParseResponse(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }

    public static String lookupUser(String assetURL) throws IOException {
        URL url = new URL(String.format("%s?%s=%s&%s=%s&%s=%s&%s",
                BASE_URL,
                METHOD_PARAM, FLICKR_URLS_LOOKUP_USER,
                API_KEY_PARAM, API_KEY_VALUE,
                URL_PARAM, assetURL,
                FORMAT_JSON));

        String response = getAndParseResponse(url);

        // Parse out userID from response
        response = response.split(":")[2];
        String user = response.split(",")[0];
        user = user.substring(1, user.length()-1);
        return user;
    }

    public static String getCollectionTree(String collectionId, String userId) throws IOException {
        URL url = new URL(String.format("%s?%s=%s&%s=%s&%s=%s&%s=%s&%s",
                BASE_URL,
                METHOD_PARAM, COLLECTIONS_GET_TREE,
                API_KEY_PARAM, API_KEY_VALUE,
                COLLECTION_ID_PARAM, collectionId,
                USER_ID_PARAM, userId,
                FORMAT_JSON));

        String content = getAndParseResponse(url);
        String jsonString = content.substring(14, content.length()-1);
        return jsonString;
    }

    public static JSONArray listAlbumPhotos(String photoSetId, String userId) throws IOException {
        URL url = new URL(String.format("%s?%s=%s&%s=%s&%s=%s&%s=%s&%s",
                BASE_URL,
                METHOD_PARAM, GET_PHOTOS,
                API_KEY_PARAM, API_KEY_VALUE,
                PHOTOSET_ID_PARAM, photoSetId,
                USER_ID_PARAM, userId,
                FORMAT_JSON));

        String response = getAndParseResponse(url);
        response = response.substring(14, response.length()-1);
        JSONObject photoSet = new JSONObject(response);
        return photoSet.getJSONObject("photoset").getJSONArray("photo");
    }

    public static JSONObject getPhotoInfo(String id) throws IOException {
        URL url = new URL(String.format("%s?%s=%s&%s=%s&%s=%s&%s",
                BASE_URL,
                METHOD_PARAM, PHOTOS_GET_INFO,
                API_KEY_PARAM, API_KEY_VALUE,
                PHOTO_ID_PARAM, id,
                FORMAT_JSON));

        String response = getAndParseResponse(url);
        String jsonString = response.substring(14, response.length()-1);
        return new JSONObject(jsonString);
    }



    public static JSONObject getPhotoSizes(String id) throws IOException {
        URL url = new URL(String.format("%s?%s=%s&%s=%s&%s=%s&%s",
                BASE_URL,
                METHOD_PARAM, PHOTOS_GET_SIZES,
                API_KEY_PARAM, API_KEY_VALUE,
                PHOTO_ID_PARAM, id,
                FORMAT_JSON));

        String photoSizeResponse = getAndParseResponse(url);
        photoSizeResponse = photoSizeResponse.substring(14, photoSizeResponse.length()-1);
        return new JSONObject(photoSizeResponse);
    }
}
