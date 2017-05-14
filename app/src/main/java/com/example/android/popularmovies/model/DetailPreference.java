package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by monika on 2017-05-10.
 */

/**
 * Enums for choosing trailers or reviews; we will use them to call MovieDB service
 */
public enum DetailPreference implements Parcelable {
    TRAILER("TRAILER"), REVIEW("REVIEW");

    public static final Parcelable.Creator<DetailPreference> CREATOR = new Parcelable.Creator<DetailPreference>() {
        @Override
        public DetailPreference createFromParcel(Parcel in) {
            DetailPreference option = DetailPreference.values()[in.readInt()];
            option.setOption(in.readString());
            return option;
        }

        @Override
        public DetailPreference[] newArray(int size) {
            return new DetailPreference[size];
        }
    };
    private String option;

    DetailPreference(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getName() {
        return option;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(ordinal());
        out.writeString(option);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}