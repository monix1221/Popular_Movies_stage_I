package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by monika on 2017-05-07.
 */

/**
 * Defins what the database looks like for the rest of the application
 */
public class MovieContract{

    /* Because we are implementing MovieContentProvider, we need to add here URIs for the paths
        1)Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the TaskEntry class
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies"; //the same authority as we defined in Android Manifest
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY); //content scheme + authority
    public static final String PATH_TASKS = "favoriteMovies"; //the name of our SQLite table

    public static final class MovieEntry  implements BaseColumns {

        //these CONTENT_URI and CONTENT_TYPE are needed for content provider
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASKS;

        //for content provider:
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


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