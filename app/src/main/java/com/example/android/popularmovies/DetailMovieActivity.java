package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieDbHelper;
import com.example.android.popularmovies.databinding.ActivityDetailMovieBinding;
import com.example.android.popularmovies.model.ItemModel;
import com.example.android.popularmovies.model.Keys;
import com.squareup.picasso.Picasso;

public class DetailMovieActivity extends AppCompatActivity {

    //a field that will be used for binding views from activity_detail_movie.xml
    ActivityDetailMovieBinding binding;
    private ItemModel itemMovieModel;

    private SQLiteDatabase mDb;     //database saving movies marked as favourites

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
            itemMovieModel.setId(intentThatStartedThisActivity.getIntExtra((MainActivity.MOVIE_ID), 0));
        }

        if (itemMovieModel != null) {
            bindDetailMovie(itemMovieModel);
        }

        binding.buttonAddFavMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDeleteFromFavButtonVisible();
                addMovieToFavorites(itemMovieModel.getId(), itemMovieModel);
                Toast.makeText(DetailMovieActivity.this, R.string.toast_added_to_fav, Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonDeleteFavMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddToFavButtonVisible();
                removeFavouriteMovie(itemMovieModel.getId());
                Toast.makeText(DetailMovieActivity.this, R.string.toast_deleted_to_fav, Toast.LENGTH_SHORT).show();
            }
        });

        MovieDbHelper dbHelper = new MovieDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        Cursor cursor;
        cursor = getAllMovies();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                if (id == itemMovieModel.getId()) {
                    setDeleteFromFavButtonVisible();
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void setAddToFavButtonVisible() {
        binding.buttonAddFavMovie.setVisibility(View.VISIBLE);
        binding.buttonDeleteFavMovie.setVisibility(View.GONE);
    }

    private void setDeleteFromFavButtonVisible() {
        binding.buttonAddFavMovie.setVisibility(View.GONE);
        binding.buttonDeleteFavMovie.setVisibility(View.VISIBLE);
    }

    private Cursor getAllMovies() {
        return mDb.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MovieContract.MovieEntry.COLUMN_ID
        );
    }

    private void addMovieToFavorites(int id, ItemModel model) {

        ContentValues cv = new ContentValues();

        cv.put(MovieContract.MovieEntry.COLUMN_ID, id);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, model.getTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, model.getImgUrl());
        cv.put(MovieContract.MovieEntry.COLUMN__MOVIE_RELEASE_DATE, model.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, model.getVoteAvg());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_PLOT, model.getOverview());

        try {
            mDb.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
        } catch (Exception e) {
            Toast.makeText(this, R.string.error_adding_fav+"" + e, Toast.LENGTH_LONG).show();
        }
    }

    private void removeFavouriteMovie(int id) {
        mDb.delete(MovieContract.MovieEntry.TABLE_NAME,
                MovieContract.MovieEntry.COLUMN_ID + " = " + id, null);
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
