package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.model.ReviewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by monika on 2017-05-10.
 */

public class ReviewJsonUtils {
    /**
     * This method parses JSON from a web response and returns an ArrayList of ReviewModel
     * containing review details
     **/
    public static ArrayList<ReviewModel> getSimpleMovieStringsFromJson(String movieJsonStr)
            throws JSONException {

        final String RW_RESULTS = "results";
        final String RW_ID = "id";
        final String RW_AUTHOR = "author";
        final String RW_CONTENT = "content";
        final String RW_URL = "url";

        //checking for errors:
        final String MV_MESSAGE_CODE = "status_code";

    /* ArrayList to hold each reviews' details  */
        ArrayList<ReviewModel> parsedMovieData = null;

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

        JSONArray movieArray = movieJson.getJSONArray(RW_RESULTS);
        // here is all parsed JSON data:
        parsedMovieData = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {
            // /Get JSONObject representing a single movie data
            JSONObject object = movieArray.getJSONObject(i);
            String id = object.optString(RW_ID);
            String author = object.optString(RW_AUTHOR);
            String content = object.optString(RW_CONTENT);
            String url = object.optString(RW_URL);

            ReviewModel reviewItem = new ReviewModel();
            reviewItem.setId(id);
            reviewItem.setAuthor(author);
            reviewItem.setContent(content);
            reviewItem.setUrl(url);
            parsedMovieData.add(reviewItem);
        }

        return parsedMovieData;
    }
}