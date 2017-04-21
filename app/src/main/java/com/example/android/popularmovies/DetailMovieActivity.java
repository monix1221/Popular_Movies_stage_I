package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.ItemModel;
import com.example.android.popularmovies.model.Keys;
import com.squareup.picasso.Picasso;


public class DetailMovieActivity extends AppCompatActivity {

    private ItemModel itemMovieModel;

    private ImageView mDetailPoster;

    private TextView mDetailTitle;
    private TextView mDetailReleaseDate;
    private TextView mDetailVoteAverage;
    private TextView mDetailOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        mDetailPoster = (ImageView) findViewById(R.id.iv_detail_poster);
        mDetailTitle = (TextView) findViewById(R.id.tv_detail_title);
        mDetailReleaseDate = (TextView) findViewById(R.id.tv_detail_release_date);
        mDetailVoteAverage = (TextView) findViewById(R.id.tv_detail_vote_average);
        mDetailOverview = (TextView) findViewById(R.id.tv_detail_overview);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {

                itemMovieModel = new ItemModel();

                itemMovieModel.setImgUrl(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_IMAGE));
                itemMovieModel.setOriginalTitle(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_TITLE));
                itemMovieModel.setOverview(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_SYNOPSIS));
                itemMovieModel.setReleaseDate(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_RELEASE_DATE));
                itemMovieModel.setVoteAvg(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_VOTES));
        }

        if (itemMovieModel != null){
            bindDetailMovie(itemMovieModel);
        }

    }

    private void bindDetailMovie(ItemModel movie){
        mDetailTitle.setText(movie.getOriginalTitle());
        mDetailReleaseDate.setText(R.string.release_date+"\n"+movie.getReleaseDate());
        mDetailVoteAverage.setText(R.string.vote_average+"\n"+movie.getVoteAvg()+R.string.ten);
        mDetailOverview.setText(movie.getOverview());

        final String IMAGE_PATH = Keys.IMAGE_BASE_PATH+movie.getImgUrl();

        Picasso.with(this)
                .load(IMAGE_PATH)
                .fit().centerInside()
                .placeholder(R.drawable.poster_placeholder)
                .into(mDetailPoster);
    }

}
