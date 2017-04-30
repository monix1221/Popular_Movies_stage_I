package com.example.android.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android.popularmovies.databinding.ActivityDetailMovieBinding;
import com.example.android.popularmovies.model.ItemModel;
import com.example.android.popularmovies.model.Keys;
import com.squareup.picasso.Picasso;

public class DetailMovieActivity extends AppCompatActivity {

    //a field that will be used for binding views from activity_detail_movie.xml
    ActivityDetailMovieBinding binding;
    private ItemModel itemMovieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_movie);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            itemMovieModel = new ItemModel();

            itemMovieModel.setImgUrl(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_IMAGE));
            itemMovieModel.setTitle(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_TITLE));
            itemMovieModel.setOverview(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_SYNOPSIS));
            itemMovieModel.setReleaseDate(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_RELEASE_DATE));
            itemMovieModel.setVoteAvg(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_VOTES));
        }

        if (itemMovieModel != null) {
            bindDetailMovie(itemMovieModel);
        }
    }

    private void bindDetailMovie(ItemModel movie) {

        binding.tvDetailTitle.setText(movie.getTitle());
        binding.tvDetailReleaseDate.setText(getString(R.string.release_date) + "\n" + movie.getReleaseDate());
        binding.tvDetailVoteAverage.setText(getString(R.string.vote_average) + "\n" + movie.getVoteAvg() + getString(R.string.ten));
        binding.tvDetailOverview.setText(movie.getOverview());

        final String IMAGE_PATH = Keys.IMAGE_BASE_PATH + movie.getImgUrl();

        Picasso.with(this)
                .load(IMAGE_PATH)
                .fit().centerInside()
                .placeholder(R.drawable.poster_placeholder)
                .into(binding.ivDetailPoster);
    }
}
