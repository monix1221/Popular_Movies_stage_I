package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.model.TrailerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by monika on 2017-05-10.
 */

public class TrailerJsonUtils {

    /**
     * This method parses JSON from a web response and returns an ArrayList of TrailerModel
     * containing trailer details
     **/
    public static ArrayList<TrailerModel> getSimpleMovieStringsFromJson(String movieJsonStr)
            throws JSONException {

        final String TR_RESULTS = "results";
        final String TR_ID = "id";
        final String TR_KEY = "key";
        final String TR_NAME = "name";
        final String TR_SIZE = "size";
        final String TR_SITE = "site";

        //checking for errors: could be
        // {"status_code":34,"status_message":"The resource you requested could not be found."}
        final String MV_MESSAGE_CODE = "status_code";

    /* ArrayList to hold each movies' details  */
        ArrayList<TrailerModel> parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);

     /* Is there an error? */
        if (movieJson.has(MV_MESSAGE_CODE)) {
            int errorCode = movieJson.getInt(MV_MESSAGE_CODE);
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Query invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray movieArray = movieJson.getJSONArray(TR_RESULTS);
        // here is all parsed JSON data:
        parsedMovieData = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {
            // /Get JSONObject representing a single movie data
            JSONObject object = movieArray.getJSONObject(i);
            String id = object.optString(TR_ID);
            String key = object.optString(TR_KEY);
            String name = object.optString(TR_NAME);
            String site = object.optString(TR_SITE);
            String size = object.optString(TR_SIZE);

            TrailerModel trailerItem = new TrailerModel();
            trailerItem.setId(id);
            trailerItem.setKey(key);
            trailerItem.setName(name);
            trailerItem.setSite(site);
            trailerItem.setSize(size);
            parsedMovieData.add(trailerItem);
        }
        return parsedMovieData;
    }
}