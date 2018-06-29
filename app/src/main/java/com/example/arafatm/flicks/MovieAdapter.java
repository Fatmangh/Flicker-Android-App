package com.example.arafatm.flicks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.arafatm.flicks.models.Movie;
import com.example.arafatm.flicks.models.config;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //list of movies
    ArrayList<Movie> movies;
    //config needed for imageUrl
    config Config;
    //context for rendering
    Context context;

    public void setConfig(config config) {
        Config = config;
    }

    //initialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    //creates and inflates a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    //binds an inflated niew to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the movie data at position
        Movie movie = movies.get(position);
        //populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //build url
        String imageUrl = Config.getImageUrl(Config.getPosterSize(), movie.getPosterPath());
        //load image using glide

        Glide.with(context)
                .load(imageUrl)
                .apply(
                        RequestOptions.placeholderOf(R.drawable.flicks_movie_placeholder)
                                .error(R.drawable.flicks_movie_placeholder)
                                .fitCenter()
                                .transform(new RoundedCornersTransformation(20, 0))
                ).into(holder.ivPosterImage);

    }

    //returns the size of the entire movie lise
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the viewHolder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPosterImage = itemView.findViewById(R.id.imageView);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            // add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        // when the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }

    }
}
