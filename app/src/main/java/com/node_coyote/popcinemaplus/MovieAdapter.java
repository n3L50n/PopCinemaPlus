package com.node_coyote.popcinemaplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.node_coyote.popcinemaplus.data.MovieContract.MovieEntry;
import com.squareup.picasso.Picasso;


/**
 * Created by node_coyote on 5/1/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ContentValues[] mMovieData;
    private Cursor mCursor;

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

//            String[] movieData = {
//                    mMovieData.get(adapterPosition).getTitle(),
//                    mMovieData.get(adapterPosition).getSummary(),
//                    mMovieData.get(adapterPosition).getRelease(),
//                    mMovieData.get(adapterPosition).getPosterPath(),
//                    mMovieData.get(adapterPosition).getTopRated(),
//                    mMovieData.get(adapterPosition).getMovieId()
//            };

            //mClickHandler.onClick(movieData);
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

        // Set the views of the main page grid items with title and poster image.

        mCursor.moveToPosition(position);

        int posterColumnIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_POSTER);

        String posterPath = mCursor.getString(posterColumnIndex);
        String posterBaseUrl = "http://image.tmdb.org/t/p/w342/";
        String fullPosterUrl = posterBaseUrl + posterPath;

        if (!fullPosterUrl.isEmpty() && fullPosterUrl.length() != 0) {
            Picasso.with(holder.mGridMoviePoster.getContext()).load(fullPosterUrl).into(holder.mGridMoviePoster);
        }

    }

    @Override
    public int getItemCount() {
        // Check for empty data to prevent crash
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public void setMovieData(ContentValues[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
