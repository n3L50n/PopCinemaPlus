package com.node_coyote.popcinema;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.node_coyote.popcinema.data.MovieContract.MovieEntry;

/**
 * Created by node_coyote on 5/17/17.
 */

public class MovieGridCursorAdapter extends CursorAdapter {

    public MovieGridCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_poster_grid_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView gridMoviePoster = (ImageView) view.findViewById(R.id.grid_movie_poster);

        int gridPosterPathIndex = cursor.getColumnIndex(MovieEntry.COLUMN_POSTER);

        String posterPath = cursor.getString(gridPosterPathIndex);


    }

}
