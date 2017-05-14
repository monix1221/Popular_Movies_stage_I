package com.example.android.popularmovies.model;

/**
 * Created by monika on 2017-04-20.
 */

public abstract class Keys {

    /*
    ATTENTION! Place your API KEY below.
    In case you don't have your API KEY, you can ask for one on: https://www.themoviedb.org/
     */
    public static final String API_KEY = "";

    public static final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/w185/";
    public static final String SORT_BY_POPULAR_URL = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + API_KEY;
    public static final String SORT_BY_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key=" + API_KEY;
    public static final String DETAIL_BASE_PATH = "https://api.themoviedb.org/3/movie/";
    public static final String TRAILER_END_PATH = "/videos?api_key=";
    public static final String REVIEW_END_PATH = "/reviews?api_key=";
    public static final String YOUTUBE_BASE_PATH = "http://img.youtube.com/vi/";
    public static final String YOUTUBE_END_PATH = "/0.jpg";
    public static final String YOUTUBE_WEB_BASE_URL = "http://www.youtube.com/watch?v=";
    public static final String YOUTUBE_APP_BASE_URL = "vnd.youtube://";

}