package com.example.android.popularmovies.model;


/**
 * Created by monika on 2017-04-20.
 */

public class ItemModel {

  /*
  * Class represents each movie-item data;
  * using methods of this class we will be able to get each movies' needed details
   */

    private String imgUrl;
    private String original_title;
    private String id;
    private String release_date;
    private String vote_average;
    private String overview;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String title) {
        this.original_title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

}













