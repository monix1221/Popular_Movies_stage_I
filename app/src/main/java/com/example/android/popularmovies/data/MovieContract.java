package com.example.android.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by monika on 2017-05-07.
 */

/**
 * Defins what the database looks like for the rest of the application
 */
public class MovieContract{

    public static final class MovieEntry  implements BaseColumns {

        //BaseColumns interface already have a field _ID so we don't need to create it

        public static final String TABLE_NAME="favoriteMovies";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_MOVIE_TITLE="title";
        public static final String COLUMN_MOVIE_POSTER="poster";
        public static final String COLUMN__MOVIE_RELEASE_DATE="releaseDate";
        public static final String COLUMN_MOVIE_RATING="rating";
        public static final String COLUMN_MOVIE_PLOT="plot";

    }
}