package com.example.android.popularmovies.utilities;

import android.net.Uri;

import com.example.android.popularmovies.model.Keys;
import com.example.android.popularmovies.model.MoviePreferences;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


/**
 * Created by monika on 2017-04-16.
 */

public class NetworkUtils {

    private static String MOVIE_URL = "";

    public static URL buildUrl(MoviePreferences preferredSortOrder) {
        // building the proper URL query
        switch(preferredSortOrder){
            case POPULAR:
                MOVIE_URL = Keys.SORT_BY_POPULAR_URL;
                break;
            case TOP_RATED:
                MOVIE_URL = Keys.SORT_BY_TOP_RATED;
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
