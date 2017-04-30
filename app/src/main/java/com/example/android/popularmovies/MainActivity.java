package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.example.android.popularmovies.model.ItemModel;
import com.example.android.popularmovies.model.MoviePreferences;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ImageAdapterOnClickHandler {

    //needed only for intent
    public static final String MOVIE_TITLE = "movie_title";
    public static final String MOVIE_IMAGE = "movie_image";
    public static final String MOVIE_RELEASE_DATE = "movie_release_date";
    public static final String MOVIE_VOTES = "movie_votes";
    public static final String MOVIE_SYNOPSIS = "movie_synopsis";

    //a field for binding views data in activity_main.xml
    ActivityMainBinding binding;

    private ImageAdapter mImageAdapter;
    private ArrayList<ItemModel> gridItemsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rvImages.setLayoutManager(gridLayoutManager);
        binding.rvImages.setHasFixedSize(true);

        gridItemsData = new ArrayList<>();
        mImageAdapter = new ImageAdapter(MainActivity.this, gridItemsData, this);
        binding.rvImages.setAdapter(mImageAdapter);

        loadMovieData(MoviePreferences.POPULAR);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    private void loadMovieData(MoviePreferences preferences) {
        new MovieQueryTask().execute(preferences);
    }

    private void setListToTopRated() {
        mImageAdapter.setMovieData(null);
        loadMovieData(MoviePreferences.TOP_RATED);
    }

    private void setListToPopular() {
        mImageAdapter.setMovieData(null);
        loadMovieData(MoviePreferences.POPULAR);
    }

    private void showMovieDataGridView() {
        binding.tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        binding.rvImages.setVisibility(View.VISIBLE);

    }

    private void showErrorMessage() {
        binding.tvErrorMessageDisplay.setVisibility(View.VISIBLE);
        binding.rvImages.setVisibility(View.INVISIBLE);
    }

    //building menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();

        switch (itemClicked) {

            case R.id.action_refresh:
                mImageAdapter.setMovieData(null);
                setListToPopular();
                return true;

            case R.id.sort_by_popular:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                mImageAdapter.setMovieData(null);

                setListToPopular();
                String textToShow = getString(R.string.sorted_popular);
                Toast.makeText(MainActivity.this, textToShow, Toast.LENGTH_SHORT).show();
                return true; //must say true here!

            case R.id.sort_by_top_rated:
                mImageAdapter.setMovieData(null);
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                setListToTopRated();
                String textToShow2 = getString(R.string.sorted_top_rated);
                Toast.makeText(MainActivity.this, textToShow2, Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(ItemModel movieResults) {
        Context context = this;
        Class destinationActivity = DetailMovieActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationActivity);

        intentToStartDetailActivity.putExtra(MOVIE_IMAGE, movieResults.getImgUrl());
        intentToStartDetailActivity.putExtra(MOVIE_TITLE, movieResults.getTitle());
        intentToStartDetailActivity.putExtra(MOVIE_SYNOPSIS, movieResults.getOverview());
        intentToStartDetailActivity.putExtra(MOVIE_RELEASE_DATE, movieResults.getReleaseDate());
        intentToStartDetailActivity.putExtra(MOVIE_VOTES, movieResults.getVoteAvg());
        startActivity(intentToStartDetailActivity);
    }

    //Inner class to handle background task
    public class MovieQueryTask extends AsyncTask<MoviePreferences, Void, ArrayList<ItemModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.pbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<ItemModel> doInBackground(MoviePreferences... preferredSortOrder) {
            if (preferredSortOrder.length == 0) {
                return null;
            }
            MoviePreferences sortOrderPreference = preferredSortOrder[0];

            URL searchUrl = NetworkUtils.buildUrl(sortOrderPreference);
            String movieSearchResults = null;
            try {
                movieSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                ArrayList<ItemModel> simpleJsonMovieData =
                        MovieJsonUtils.getSimpleMovieStringsFromJson(movieSearchResults);

                return simpleJsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemModel> movieResults) {
            binding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieResults != null) {
                mImageAdapter.setMovieData(movieResults);
                showMovieDataGridView();

            } else {
                showErrorMessage();
            }
        }
    }
}