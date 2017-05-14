package com.example.android.popularmovies.utilities;

import android.os.AsyncTask;

import com.example.android.popularmovies.model.DetailPreference;;
import com.example.android.popularmovies.model.MoviePref;
import com.example.android.popularmovies.model.TrailerModel;

import java.net.URL;
import java.util.ArrayList;


/**
 * Created by monika on 2017-05-10.
 */

public class FetchTrailersTask extends AsyncTask<MoviePref, Void, ArrayList<TrailerModel>> {

    /**
     * We will implement FetchTrailerTask.Listener in DetailMovieActivity in order to override onFetchFinish and pass ArrayList<TrailerModel>
     */
    private final Listener mListener;

    public FetchTrailersTask(Listener listener) {
        mListener = listener;
    }

    @Override
    public ArrayList<TrailerModel> doInBackground(MoviePref... params) {

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

            ArrayList<TrailerModel> simpleJsonMovieData =
                    TrailerJsonUtils.getSimpleMovieStringsFromJson(movieSearchResults);

            return simpleJsonMovieData;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<TrailerModel> trailers) {
        if (trailers != null) {
            mListener.onFetchFinished(trailers);
        } else {
            mListener.onFetchFinished(new ArrayList<TrailerModel>());
        }
    }

    public interface Listener {
        void onFetchFinished(ArrayList<TrailerModel> trailers);
    }
}