package com.node_coyote.popcinema;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.node_coyote.popcinema.utility.Movie;

import java.util.List;

/**
 * Created by node_coyote on 5/1/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private List<Movie> mMovieData;

    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * Interface to receive onClick messages
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(String[] movieData);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final ImageView mGridMoviePoster;
        public final TextView mGridMovieTitle;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mGridMovieTitle = (TextView) itemView.findViewById(R.id.grid_movie_title);
            mGridMoviePoster = (ImageView) itemView.findViewById(R.id.grid_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            //String[] d = new String[mMovieData.get(adapterPosition).getTitle()]{};
            String[] movieData = {mMovieData.get(adapterPosition).getTitle(),
                    mMovieData.get(adapterPosition).getSummary(),
                    mMovieData.get(adapterPosition).getRelease()};

            mClickHandler.onClick(movieData);
        }
    }

    public MovieAdapter(MovieAdapterOnClickHandler handler) {
        mClickHandler = handler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int gridItemId = R.layout.movie_poster_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParentImmediately = false;

        View view = inflater.inflate(gridItemId, parent, attachToParentImmediately);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        // Display Poster above Title for a grid item here. Good place to put the poster_path.
        String movie = mMovieData.get(position).getTitle();
        holder.mGridMovieTitle.setText(movie);
    }

    @Override
    public int getItemCount() {
        // Check for empty data to prevent crash
        if (mMovieData == null) return 0;
        return mMovieData.size();
    }

    public void setMovieData(List<Movie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
