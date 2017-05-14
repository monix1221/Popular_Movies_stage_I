package com.example.android.popularmovies.utilities;

import android.os.AsyncTask;

import com.example.android.popularmovies.model.DetailPreference;
import com.example.android.popularmovies.model.MoviePref;
import com.example.android.popularmovies.model.ReviewModel;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by monika on 2017-05-11.
 */

public class FetchReviewTask extends AsyncTask<MoviePref, Void, ArrayList<ReviewModel>> {
    /**
     * We will implement FetchTrailerTask.Listener in DetailMovieActivity in order to override onFetchFinish and pass ArrayList<ReviewModel>
     */
    private final Listener mListener;

    public FetchReviewTask(Listener listener) {
        mListener = listener;
    }

    @Override
    public ArrayList<ReviewModel> doInBackground(MoviePref... params) {

        if (params.equals(0)) {
            return null;
        }

        MoviePref pr = params[0];
        int movieId = pr.getId();
        DetailPreference pref = pr.getPreference();

        URL searchUrl = NetworkDetailsUtils.buildUrl(movieId, pref);
        String movieSearchResults = null;
        try {
            movieSearchResults = NetworkDetailsUtils.getResponseFromHttpUrl(searchUrl);

            ArrayList<ReviewModel> simpleJsonMovieData =
                    ReviewJsonUtils.getSimpleMovieStringsFromJson(movieSearchResults);

            return simpleJsonMovieData;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ReviewModel> reviews) {
        if (reviews != null) {
            mListener.onReviewsFetchFinished(reviews);
        } else {
            mListener.onReviewsFetchFinished(new ArrayList<ReviewModel>());
        }
    }

    public interface Listener {
        void onReviewsFetchFinished(ArrayList<ReviewModel> reviews);
    }
}