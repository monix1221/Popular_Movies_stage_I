package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by monika on 2017-04-20.
 */

public class ItemModel implements Parcelable {

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };
    /*
    * Class represents each movie-item data;
    * using methods of this class we will be able to get each movies' needed details
     */
    String imgUrl;
    String title;
    int id;
    String release_date;
    String vote_average;
    String overview;

    public ItemModel(Parcel in) {
        id = in.readInt();
        imgUrl = in.readString();
        title = in.readString();
        release_date = in.readString();
        vote_average = in.readString();
        overview = in.readString();
    }

    public ItemModel() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String releasedate) {
        this.release_date = releasedate;
    }

    public String getVoteAvg() {
        return vote_average;
    }

    public void setVoteAvg(String voteavg) {
        this.vote_average = voteavg;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeString(title);
        dest.writeInt(id);
        dest.writeString(release_date);
        dest.writeString(vote_average);
        dest.writeString(overview);
    }
}