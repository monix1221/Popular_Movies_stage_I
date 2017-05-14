package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.ReviewListBinding;
import com.example.android.popularmovies.model.ReviewModel;

import java.util.ArrayList;

/**
 * Created by monika on 2017-05-11.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> implements View.OnClickListener {

    //ClickHandler to handle when the user clicks on the trailer in DetailMovieActivity
    private final ReviewAdapterOnClickHandler mClickHandler;
    private int mExpandedPosition = -1;
    private Context context;
    private ArrayList<ReviewModel> mReviewItemList;

    public ReviewAdapter(Context context, ArrayList<ReviewModel> reviewItem, ReviewAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.mReviewItemList = reviewItem;
        this.mClickHandler = clickHandler;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //We are using data binding here
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ReviewListBinding itemBinding = ReviewListBinding.inflate(layoutInflater, viewGroup, false);
        return new ReviewAdapterViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(final ReviewAdapterViewHolder holder, final int position) {

        final boolean isExpanded = position == mExpandedPosition;

        final ReviewModel reviewData = mReviewItemList.get(position);
        holder.bind(context, reviewData);

        holder.binding.tvReviewContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                if (!isExpanded) {
                    holder.binding.tvReviewContent.setMaxLines(10000000);
                    holder.binding.tvReadMoreReview.setText(R.string.read_less);
                } else {
                    holder.binding.tvReviewContent.setMaxLines(5);
                    holder.binding.tvReadMoreReview.setText(R.string.read_more);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mReviewItemList == null) return 0;
        return mReviewItemList.size();
    }

    public void setMovieData(ArrayList<ReviewModel> reviewData) {
        mReviewItemList = reviewData;
        notifyDataSetChanged();
    }

    //2.05.2017

    public ReviewModel getItem(int i) {
        ReviewModel movieItem = null;
        if (mReviewItemList != null)
            movieItem = mReviewItemList.get(i);

        return movieItem;
    }

    @Override
    public void onClick(View v) {
    }

    public ArrayList<ReviewModel> getMoviesList() {
        return mReviewItemList;
    }

    public void add(ArrayList<ReviewModel> reviews) {
        mReviewItemList.clear();
        mReviewItemList.addAll(reviews);
        notifyDataSetChanged();
    }

    //interface to handle user's clicks on the image in Detail Activity
    public interface ReviewAdapterOnClickHandler {
        void onClick(ReviewModel movie);
    }

    //inner class:
    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //needed for binding: so we don't need to make here TextViews
        private final ReviewListBinding binding;


        public ReviewAdapterViewHolder(ReviewListBinding binding) {
            //using binding, so we don't need to use here findViewById
            super(binding.getRoot());
            this.binding = binding;
            binding.tvReviewContent.setOnClickListener(this);
            binding.tvReadMoreReview.setOnClickListener(this);
        }

        private void bind(Context context, ReviewModel review) {
            binding.tvReviewContent.setText(review.getContent());
            binding.tvAuthorReview.setText(review.getAuthor());
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            ReviewModel movie = mReviewItemList.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }
}