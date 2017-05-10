package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.model.ItemModel;
import com.example.android.popularmovies.model.Keys;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by monika on 2017-04-17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterViewHolder> {

    //ClickHandler to handle when the user clicks on the image in MainActivity
    private final ImageAdapterOnClickHandler mClickHandler;
    private Context context;
    private ArrayList<ItemModel> mGridItemMovieData;

    //constructor
    //must add another parametr in constructor: clickHandler
    public ImageAdapter(Context context, ArrayList<ItemModel> gridItem, ImageAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.mGridItemMovieData = gridItem;
        this.mClickHandler = clickHandler;
    }

    @Override
    public ImageAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.image_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ImageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapterViewHolder holder, int position) {
        ItemModel movieData = mGridItemMovieData.get(position);

        holder.bind(context, movieData);
    }

    @Override
    public int getItemCount() {
        if (mGridItemMovieData == null) return 0;
        return mGridItemMovieData.size();
    }

    public void setMovieData(ArrayList<ItemModel> movieData) {
        mGridItemMovieData = movieData;
        notifyDataSetChanged();
    }

    //2.05.2017

    public ItemModel getItem(int i) {
        ItemModel movieItem = null;
        if(mGridItemMovieData != null)
            movieItem = mGridItemMovieData.get(i);

        return movieItem;
    }

    //interface to handle user's clicks on the image in Main Activity
    public interface ImageAdapterOnClickHandler {
        void onClick(ItemModel movie);
    }

    //inner class:
    public class ImageAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mGridItemView;

        //constructor:
        public ImageAdapterViewHolder(View view) {
            super(view);
            mGridItemView = (ImageView) view.findViewById(R.id.image_grid_item);
            view.setOnClickListener(this);
        }

        private void bind(Context context, ItemModel movie) {

            String IMAGE_BASE_PATH = Keys.IMAGE_BASE_PATH;
            String imagePath = IMAGE_BASE_PATH + movie.getImgUrl();

            Picasso.with(context)
                    .load(imagePath)
                    .fit()
                    .placeholder(R.drawable.poster_placeholder)
                    .into(mGridItemView);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            ItemModel movie = mGridItemMovieData.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }


    public ArrayList<ItemModel> getMoviesList() {
        return mGridItemMovieData;
    }
}