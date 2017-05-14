package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Keys;
import com.example.android.popularmovies.model.TrailerModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by monika on 2017-05-10.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> implements View.OnClickListener {

    //ClickHandler to handle when the user clicks on the trailer in DetailMovieActivity
    private final TrailerAdapterOnClickHandler mClickHandler;
    private Context context;
    private ArrayList<TrailerModel> mTrailerItem;

    public TrailerAdapter(Context context, ArrayList<TrailerModel> trailerItem, TrailerAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.mTrailerItem = trailerItem;
        this.mClickHandler = clickHandler;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        TrailerModel trailerData = mTrailerItem.get(position);
        holder.bind(context, trailerData);
    }

    @Override
    public int getItemCount() {
        if (mTrailerItem == null) return 0;
        return mTrailerItem.size();
    }

    public void setMovieData(ArrayList<TrailerModel> trailerData) {
        mTrailerItem = trailerData;
        notifyDataSetChanged();
    }

    public TrailerModel getItem(int i) {
        TrailerModel movieItem = null;
        if (mTrailerItem != null)
            movieItem = mTrailerItem.get(i);

        return movieItem;
    }

    @Override
    public void onClick(View v) {

    }

    public ArrayList<TrailerModel> getMoviesList() {
        return mTrailerItem;
    }

    public void add(ArrayList<TrailerModel> trailers) {
        mTrailerItem.clear();
        mTrailerItem.addAll(trailers);
        notifyDataSetChanged();
    }

    //interface to handle user's clicks on the image in Detail Activity
    public interface TrailerAdapterOnClickHandler {
        void onClick(TrailerModel movie);
    }

    //inner class:
    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mTrailerView;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerView = (ImageView) view.findViewById(R.id.trailer_item_thumbnail);
            view.setOnClickListener(this);
        }

        private void bind(Context context, TrailerModel trailer) {

            String imagePath = Keys.YOUTUBE_BASE_PATH + trailer.getKey() + Keys.YOUTUBE_END_PATH;

            Picasso.with(context)
                    .load(imagePath)
                    .fit()
                    .placeholder(R.drawable.poster_placeholder)
                    .into(mTrailerView);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            TrailerModel movie = mTrailerItem.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }
}