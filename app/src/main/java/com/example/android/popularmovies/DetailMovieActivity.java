package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieDbHelper;
import com.example.android.popularmovies.databinding.ActivityDetailMovieBinding;
import com.example.android.popularmovies.model.DetailPreference;
import com.example.android.popularmovies.model.ItemModel;
import com.example.android.popularmovies.model.Keys;
import com.example.android.popularmovies.model.MoviePref;
import com.example.android.popularmovies.model.ReviewModel;
import com.example.android.popularmovies.model.TrailerModel;
import com.example.android.popularmovies.utilities.FetchReviewTask;
import com.example.android.popularmovies.utilities.FetchTrailersTask;
import com.example.android.popularmovies.utilities.ReviewAdapter;
import com.example.android.popularmovies.utilities.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class DetailMovieActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler,
        FetchTrailersTask.Listener, ReviewAdapter.ReviewAdapterOnClickHandler,
        FetchReviewTask.Listener {

    //a field that will be used for binding views from activity_detail_movie.xml --> no need to use heave findViewById anymore
    ActivityDetailMovieBinding binding;
    private ItemModel itemMovieModel;

    //needed for trailers
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<TrailerModel> mTrailerArrayData;

    //needed for reviews:
    private ReviewAdapter mReviewAdapter;
    private ArrayList<ReviewModel> mReviewArrayData;

    //database saving movies marked as favourites
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intentThatStartedThisActivity = getIntent();
        setTitle(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_TITLE));
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_movie);

        if (intentThatStartedThisActivity != null) {
            itemMovieModel = new ItemModel();

            itemMovieModel.setImgUrl(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_IMAGE));
            itemMovieModel.setTitle(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_TITLE));
            itemMovieModel.setOverview(intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_OVERVIEW));
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

        binding.buttonShareIntent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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

        //for trailers
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvTrailerList.setLayoutManager(layoutManager);
        binding.rvTrailerList.setHasFixedSize(true);

        mTrailerArrayData = new ArrayList<>();
        mTrailerAdapter = new TrailerAdapter(DetailMovieActivity.this, mTrailerArrayData, this);
        mTrailerAdapter.setMovieData(mTrailerArrayData);
        binding.rvTrailerList.setAdapter(mTrailerAdapter);

        fetchTrailers();

        //for reviews
        LinearLayoutManager layoutManagerReviews
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvReviewList.setLayoutManager(layoutManagerReviews);
        binding.rvReviewList.setHasFixedSize(true);

        mReviewArrayData = new ArrayList<>();
        mReviewAdapter = new ReviewAdapter(DetailMovieActivity.this, mReviewArrayData, this);
        mReviewAdapter.setMovieData(mReviewArrayData);
        binding.rvReviewList.setAdapter(mReviewAdapter);

        fetchTrailers();
        fetchReviews();

        createShareButton(intentThatStartedThisActivity);
    }

    public void createShareButton(final Intent intentThatStartedThisActivity) {

        //thanks to http://stackoverflow.com/questions/21329132/android-custom-dropdown-popup-menu
        binding.buttonShareIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creatin the instance of PopupMenu
                PopupMenu popup = new PopupMenu(DetailMovieActivity.this, binding.buttonShareIntent);
                //inflating the popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_for_intents, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    String send_movie_overview = intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_OVERVIEW);
                    String send_movie_title = intentThatStartedThisActivity.getStringExtra(MainActivity.MOVIE_TITLE);

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemClicked = item.getItemId();

                        switch (itemClicked) {
                            case R.id.send_email:

                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.overview) + "\n" + send_movie_overview);
                                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.checkout_movie) + " " + send_movie_title);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }
                                return true;

                            case R.id.send_as_message:

                                Intent intentMessage = new Intent(Intent.ACTION_VIEW);
                                intentMessage.setData(Uri.parse("sms:"));
                                intentMessage.putExtra("sms_body", getString(R.string.checkout_movie) + " " + send_movie_title + "\n" + getString(R.string.overview) + "\n" + send_movie_overview);
                                try {
                                    startActivity(intentMessage);
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(DetailMovieActivity.this, R.string.ooops_somethings_wrong, Toast.LENGTH_LONG).show();
                                }

                                return true;

                            case R.id.share_to_Facebook:

                                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.checkout_movie) + " " + send_movie_title + "\n" + getString(R.string.overview) + "\n" + send_movie_overview);
                                sendIntent.setType("text/plain");
                                sendIntent.setPackage("com.facebook.orca");
                                try {
                                    startActivity(sendIntent);
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(DetailMovieActivity.this, "Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
                                }
                                return true;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    private void fetchTrailers() {
        FetchTrailersTask task = new FetchTrailersTask(this);
        MoviePref pref = new MoviePref(itemMovieModel.getId(), DetailPreference.TRAILER);
        task.execute(pref);
    }

    private void fetchReviews() {
        FetchReviewTask task = new FetchReviewTask(this);
        MoviePref pref = new MoviePref(itemMovieModel.getId(), DetailPreference.REVIEW);
        task.execute(pref);
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
        Cursor cursor = getApplicationContext().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_ID},
                MovieContract.MovieEntry.COLUMN_ID + " = " + itemMovieModel.getId(),
                null,
                null);
        return cursor;
        //we are using content provider, so we dont do below (we dont query SQL DB)
        /*
        return mDb.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MovieContract.MovieEntry.COLUMN_ID
        );*/
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
            //mDb.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);    //we dont need it here because we are using content provider below
            getApplicationContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
        } catch (Exception e) {
            Toast.makeText(this, R.string.error_adding_fav + "" + e, Toast.LENGTH_LONG).show();
        }
    }

    private void removeFavouriteMovie(int id) {
        //mDb.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMN_ID + " = " + id, null);
        getApplicationContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
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

    /**
     * on click, when we click on trailer item
     *
     * @param trailer
     */
    @Override
    public void onClick(TrailerModel trailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Keys.YOUTUBE_APP_BASE_URL + trailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(Keys.YOUTUBE_WEB_BASE_URL + trailer.getKey()));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    /**
     * Method overriden from FetchTrailersTask.Listener
     * in order to add trailers do adapter
     *
     * @param trailers
     */
    @Override
    public void onFetchFinished(ArrayList<TrailerModel> trailers) {
        mTrailerAdapter.add(trailers);
    }

    @Override
    public void onReviewsFetchFinished(ArrayList<ReviewModel> reviews) {
        mReviewAdapter.add(reviews);
    }

    @Override
    public void onClick(ReviewModel movie) {

    }
}