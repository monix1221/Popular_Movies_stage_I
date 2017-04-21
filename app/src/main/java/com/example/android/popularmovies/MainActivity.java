package com.example.android.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
import com.example.android.popularmovies.model.ItemModel;
import com.example.android.popularmovies.model.MoviePreferences;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ImageAdapterOnClickHandler {

    TextView mErrorMessageDisplay;
    ProgressBar mProgressBar;

    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;

    private ArrayList<ItemModel> gridItemsData;

    //needed only for intent
    public static final String MOVIE_TITLE = "movie_title";
    public static final String MOVIE_IMAGE = "movie_image";
    public static final String MOVIE_RELEASE_DATE = "movie_release_date";
    public static final String MOVIE_VOTES = "movie_votes";
    public static final String MOVIE_SYNOPSIS = "movie_synopsis";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //Reference to RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_images);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);   //2 stands for 2 grid columns
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        gridItemsData=new ArrayList<>();

        mImageAdapter = new ImageAdapter(MainActivity.this, gridItemsData, this);
        mRecyclerView.setAdapter(mImageAdapter);

        loadMovieData(MoviePreferences.POPULAR);

    }

    private void loadMovieData(MoviePreferences preferences){
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

    //Inner class to handle background task
    public class MovieQueryTask extends AsyncTask<MoviePreferences, Void, ArrayList<ItemModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<ItemModel> doInBackground(MoviePreferences... preferredSortOrder) {
            if(preferredSortOrder.length == 0){
                return null;
            }
            MoviePreferences sortOrderPreference = preferredSortOrder[0];

            URL searchUrl = NetworkUtils.buildUrl(sortOrderPreference);
            String movieSearchResults = null;
            try {
                movieSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                ArrayList<ItemModel> simpleJsonMovieData =
                        MovieJsonUtils.getSimpleMovieStringsFromJson(MainActivity.this, movieSearchResults);

                return simpleJsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemModel> movieResults) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movieResults != null ) {
                mImageAdapter.setMovieData(movieResults);
                showMovieDataGridView();

            }else{showErrorMessage();}
        }
    }

    private void showMovieDataGridView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    private void showErrorMessage(){
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    //building menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        //returns true so the menu will be displayed in the Toolbar
        return true;
    }

    //what happens if we click on the button?
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();

        if(itemClicked == R.id.action_refresh){
            mImageAdapter.setMovieData(null);
            setListToPopular();
            return true;
        }

        if(itemClicked==R.id.action_search){

            mImageAdapter.setMovieData(null);

            new AlertDialog.Builder(this)
                    .setMessage("SORT BY:")
                    .setPositiveButton("Popular", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setListToPopular();
                            String textToShow = "Sorted by popular";
                            Toast.makeText(MainActivity.this, textToShow, Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Top-Rated", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setListToTopRated();
                    String textToShow = "Sorted by top-rated";
                    Toast.makeText(MainActivity.this, textToShow, Toast.LENGTH_SHORT).show();
                }
            }).show();

            return true; //must say true here!
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(ItemModel movieResults) {
        Context context = this;
        Class destinationActivity = DetailMovieActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationActivity);

        intentToStartDetailActivity.putExtra(MOVIE_IMAGE, movieResults.getImgUrl());
        intentToStartDetailActivity.putExtra(MOVIE_TITLE, movieResults.getOriginalTitle());
        intentToStartDetailActivity.putExtra(MOVIE_SYNOPSIS, movieResults.getOverview());
        intentToStartDetailActivity.putExtra(MOVIE_RELEASE_DATE, movieResults.getReleaseDate());
        intentToStartDetailActivity.putExtra(MOVIE_VOTES, movieResults.getVoteAvg());

        startActivity(intentToStartDetailActivity);
    }

}
