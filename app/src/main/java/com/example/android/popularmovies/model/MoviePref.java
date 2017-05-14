package com.example.android.popularmovies.model;

import java.util.ArrayList;

/**
 * Created by monika on 2017-05-10.
 */

/**
 * Class needed to easily perform FetchTrailersTask or FetchReviewTask, which needs an object that contains both id and preference
 */
public class MoviePref {

    int id;
    DetailPreference preference;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DetailPreference getPreference() {
        return preference;
    }

    public void setPreference(DetailPreference preference) {
        this.preference = preference;
    }

    public MoviePref(int id, DetailPreference preference){
        this.id=id;
        this.preference=preference;
    }
}