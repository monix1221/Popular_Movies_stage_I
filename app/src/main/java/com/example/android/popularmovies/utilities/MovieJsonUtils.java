package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.model.ItemModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by monika on 2017-04-16.
 */

public class MovieJsonUtils {

    /**
     * This method parses JSON from a web response and returns an ArrayList of IemModel
     * containing movie details
     **/
    public static ArrayList<ItemModel> getSimpleMovieStringsFromJson(String movieJsonStr)
            throws JSONException {

        final String MV_RESULTS = "results";
        final String MV_TITLE = "title";
        final String MV_POSTER_PATH = "poster_path";
        final String MV_ID = "id";
        final String MV_OVERVIEW = "overview";
        final String MV_VOTE_AVERAGE = "vote_average";
        final String MV_RELEASE_DATE = "release_date";

        //checking for errors: could be
        // {"status_code":34,"status_message":"The resource you requested could not be found."}
        final String MV_MESSAGE_CODE = "status_code";

    /* ArrayList to hold each movies' details  */
        ArrayList<ItemModel> parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);

     /* Is there an error? */
        //not sure if I should use it... the user isn't typing anything so...
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

        JSONArray movieArray = movieJson.getJSONArray(MV_RESULTS);
        // here is all parsed JSON data:
        parsedMovieData = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {
            // /Get JSONObject representing a single movie data
            JSONObject object = movieArray.getJSONObject(i);
            String title = object.optString(MV_TITLE);
            String poster = object.optString(MV_POSTER_PATH);
            String id = object.optString(MV_ID);
            String releaseDate = object.optString(MV_RELEASE_DATE);
            String voteAvg = object.optString(MV_VOTE_AVERAGE);
            String overview = object.optString(MV_OVERVIEW);

            ItemModel gridItem = new ItemModel();
            gridItem.setTitle(title);
            gridItem.setId(id);
            gridItem.setImgUrl(poster);
            gridItem.setReleaseDate(releaseDate);
            gridItem.setOverview(overview);
            gridItem.setVoteAvg(voteAvg);
            parsedMovieData.add(gridItem);
        }

        return parsedMovieData;
    }
}
