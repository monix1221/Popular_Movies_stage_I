package com.example.android.popularmovies.utilities;

import android.net.Uri;

import com.example.android.popularmovies.model.DetailPreference;
import com.example.android.popularmovies.model.Keys;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


/**
 * Created by monika on 2017-05-10.
 */

/**
 * NetworkDetailsUtils performs HTTP connection for both trailers and reviews
 */
public class NetworkDetailsUtils {
    private static String MOVIE_URL = "";

    public static URL buildUrl(int movieId, DetailPreference detailType) {

        String movieID = Integer.toString(movieId);

        switch (detailType) {
            case TRAILER:
                MOVIE_URL = Keys.DETAIL_BASE_PATH + movieID + Keys.TRAILER_END_PATH + Keys.API_KEY;
                break;
            case REVIEW:
                MOVIE_URL = Keys.DETAIL_BASE_PATH + movieID + Keys.REVIEW_END_PATH + Keys.API_KEY;
                break;
        }

        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}