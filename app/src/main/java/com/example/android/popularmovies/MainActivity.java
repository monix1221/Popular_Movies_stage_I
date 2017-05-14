package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieDbHelper;
import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.example.android.popularmovies.model.ItemModel;
import com.example.android.popularmovies.model.MoviePreferences;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ImageAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<ItemModel>> {

    //needed only for intent which will be send to DetailActivity
    public static final String MOVIE_TITLE = "movie_title";
    public static final String MOVIE_IMAGE = "movie_image";
    public static final String MOVIE_RELEASE_DATE = "movie_release_date";
    public static final String MOVIE_VOTES = "movie_votes";
    public static final String MOVIE_OVERVIEW = "movie_overview";
    public static final String MOVIE_ID = "movie_id";
    private static final String SELECTED_MOVIE_ORDER = "SelectedMovieOrder";
    private static final String LOADING_INDICATOR_STATE = "indicator_state";
    /*
     * This number will uniquely identify Loader and is chosen arbitrarily.
     * It can be any number
     */
    private static final int MOVIE_SEARCH_LOADER = 22;
    //a field for binding views data in activity_main.xml
    //binding is used to avoid using heavy findViewById
    ActivityMainBinding binding;
    MoviePreferences mSortOrderState = null;
    private ImageAdapter mImageAdapter;
    private ArrayList<ItemModel> gridItemsData;
    private SQLiteDatabase mDb;

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

        if (savedInstanceState != null) {
            mSortOrderState = savedInstanceState.getParcelable(SELECTED_MOVIE_ORDER);
        } else {
            mSortOrderState = MoviePreferences.POPULAR;
            loadMovieData(mSortOrderState);
        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        getSupportLoaderManager().initLoader(MOVIE_SEARCH_LOADER, null, this);

        MovieDbHelper dbHelper = new MovieDbHelper(this);
        mDb = dbHelper.getWritableDatabase();       // getting a reference
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SELECTED_MOVIE_ORDER, mSortOrderState);
        outState.putInt(LOADING_INDICATOR_STATE, binding.pbLoadingIndicator.getVisibility());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void loadMovieData(MoviePreferences sortOrderPreference) {
        sortOrderPreference = mSortOrderState;

        Bundle queryBundle = new Bundle();
        LoaderManager loaderManager = getSupportLoaderManager();
        // Getting Loader by calling getLoader and passing the ID we specified
        Loader<String> movieDataLoader = loaderManager.getLoader(MOVIE_SEARCH_LOADER);
        // If the Loader was null, initializing it. Else, restarting it.
        if (movieDataLoader == null) {
            loaderManager.initLoader(MOVIE_SEARCH_LOADER, queryBundle, this);
            mImageAdapter.setMovieData(null);
            loadMovieData(mSortOrderState);
        } else {
            queryBundle.putParcelable(SELECTED_MOVIE_ORDER, sortOrderPreference);
            loaderManager.restartLoader(MOVIE_SEARCH_LOADER, queryBundle, this);
        }
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
                loadMovieData(mSortOrderState);
                return true;

            case R.id.sort_by_popular:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                mImageAdapter.setMovieData(null);
                mSortOrderState = MoviePreferences.POPULAR;
                setListToPopular();

                String textToShow = getString(R.string.sorted_popular);
                Toast.makeText(MainActivity.this, textToShow, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.sort_by_top_rated:
                mImageAdapter.setMovieData(null);
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                mSortOrderState = MoviePreferences.TOP_RATED;
                setListToTopRated();

                String textToShow2 = getString(R.string.sorted_top_rated);
                Toast.makeText(MainActivity.this, textToShow2, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.my_favourites:
                mImageAdapter.setMovieData(null);
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                mImageAdapter.setMovieData(FavoriteQueries());
                showMovieDataGridView();
                binding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
                mSortOrderState = MoviePreferences.FAVOURITES_MOVIES;

                String textToShow3 = getString(R.string.your_fav_movies);
                Toast.makeText(MainActivity.this, textToShow3, Toast.LENGTH_SHORT).show();
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
        intentToStartDetailActivity.putExtra(MOVIE_OVERVIEW, movieResults.getOverview());
        intentToStartDetailActivity.putExtra(MOVIE_RELEASE_DATE, movieResults.getReleaseDate());
        intentToStartDetailActivity.putExtra(MOVIE_VOTES, movieResults.getVoteAvg());
        intentToStartDetailActivity.putExtra(MOVIE_ID, movieResults.getId());
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public Loader<ArrayList<ItemModel>> onCreateLoader(int id, final Bundle args) {
        //Return a new AsyncTaskLoader<String> as an anonymous inner class with this as the constructor's parameter
        return new AsyncTaskLoader<ArrayList<ItemModel>>(this) {

            //needed for caching the results (we won't forceLoad again if we already got the results)
            ArrayList<ItemModel> mMovieResults;

            @Override
            protected void onStartLoading() {
                // If args is null, return.
                /* If no arguments were passed, we don't have a query to perform. Simply return. */
                if (args == null) {
                    return;
                }

                /*
                 * When we initially begin loading in the background, we want to display the
                 * loading indicator to the user
                 */
                binding.pbLoadingIndicator.setVisibility(View.VISIBLE);

                if (mMovieResults != null) {
                    deliverResult(mMovieResults);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<ItemModel> loadInBackground() {

                // Get the String for our URL from the bundle passed to onCreateLoader
                MoviePreferences searchQueryUrlString = args.getParcelable(SELECTED_MOVIE_ORDER);

                if (searchQueryUrlString == null) {
                    mSortOrderState = MoviePreferences.POPULAR;
                }

                if (searchQueryUrlString == MoviePreferences.FAVOURITES_MOVIES) {
                    return FavoriteQueries();
                } else if (searchQueryUrlString != MoviePreferences.FAVOURITES_MOVIES) {
                /* Parse the URL from the passed in String and perform the search */
                    String movieSearchResults = null;
                    try {
                        URL searchUrl = NetworkUtils.buildUrl(searchQueryUrlString);
                        movieSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                        ArrayList<ItemModel> simpleJsonMovieData =
                                MovieJsonUtils.getSimpleMovieStringsFromJson(movieSearchResults);

                        return simpleJsonMovieData;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            public void deliverResult(ArrayList<ItemModel> data) {
                mMovieResults = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ItemModel>> loader, ArrayList<ItemModel> movieResults) {
        binding.pbLoadingIndicator.setVisibility(View.INVISIBLE);

        /*
         * If the results are null, we assume an error has occurred. There are much more robust
         * methods for checking errors, but we wanted to keep this particular example simple.
         */
        if (null == movieResults) {
            showErrorMessage();
        } else {
            mImageAdapter.setMovieData(movieResults);
            showMovieDataGridView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ItemModel>> loader) {
        //not using this method here, but we are required to override it here
    }

    /**
     * returns a Cursor
     *
     * @return
     */
    private Cursor getAllMovies() {
        //calling a query on mDb passing in the table name and projection String [] order by _ID
        return mDb.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,                               //projection array
                null,
                null,
                null,
                null,
                MovieContract.MovieEntry.COLUMN_ID   //the order in which we want to display; it could be also by TIME_STAMP
        );
    }

    public ArrayList<ItemModel> FavoriteQueries() {
        ArrayList<ItemModel> favoriteMovies = new ArrayList<ItemModel>();
        Cursor c = getAllMovies();
        if (c != null && c.moveToFirst()) {
            do {
                //id, title, poster, releasedate, rating, plot
                ItemModel OneFavMovie = new ItemModel();
                OneFavMovie.setId(c.getInt(0));
                OneFavMovie.setTitle(c.getString(1));
                OneFavMovie.setImgUrl(c.getString(2));
                OneFavMovie.setReleaseDate(c.getString(3));
                OneFavMovie.setVoteAvg(c.getString(4));
                OneFavMovie.setOverview(c.getString(5));
                favoriteMovies.add(OneFavMovie);
            } while (c.moveToNext());
        }
        return favoriteMovies;
    }
}