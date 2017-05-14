package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by monika on 2017-04-20.
 */

public enum MoviePreferences implements Parcelable {

    POPULAR("POPULAR"), TOP_RATED("TOP_RATED"), FAVOURITES_MOVIES("FAVOURITES_MOVIES");
    public static final Parcelable.Creator<MoviePreferences> CREATOR = new Parcelable.Creator<MoviePreferences>() {

        public MoviePreferences createFromParcel(Parcel in) {
            MoviePreferences option = MoviePreferences.values()[in.readInt()];
            option.setOption(in.readString());
            return option;
        }

        public MoviePreferences[] newArray(int size) {
            return new MoviePreferences[size];
        }

    };
    private String option;

    MoviePreferences(String option) {
        this.option = option;
    }

    public String getName() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(ordinal());
        out.writeString(option);
    }
}