package com.node_coyote.popcinemaplus;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.node_coyote.popcinemaplus.data.MovieContract.MovieEntry;
import com.node_coyote.popcinemaplus.data.MovieDatabaseHelper;
import com.node_coyote.popcinemaplus.utility.JsonUtility;
import com.node_coyote.popcinemaplus.utility.NetworkUtility;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public ContentValues[] mTrailerResults;
    public ContentValues mReviewResults;
    public URL mTrailerUrlSet;
    public URL mReviewUrlSet;
    public ReviewAdapter mAdapter;
    public TextView noReviews;
    private Uri mCurrentMovieUri;
    private TextView mMovieTitleTextView;
    private TextView mMovieSummaryTextView;
    private TextView mMovieReleaseTextView;
    private TextView mMovieRatedTextView;
    private ImageView mMoviePosterView;
    private RecyclerView mReviewRecycler;
    private int mMovieId;

    private static final int MOVIE_DETAIL_LOADER = 2;

    private static final String[] MOVIE_PROJECTION = {
            MovieEntry._ID,
            MovieEntry.COLUMN_POSTER,
            MovieEntry.COLUMN_SUMMARY,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_MOVIE_ID,
            MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_POPULARITY,
            MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieEntry.COLUMN_TRAILER_SET,
            MovieEntry.COLUMN_TRAILER,
            MovieEntry.COLUMN_FAVORITE,
            MovieEntry.COLUMN_REVIEW_SET,
            MovieEntry.COLUMN_AUTHOR,
            MovieEntry.COLUMN_CONTENT
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Find movie Detail TextViews
        mMovieTitleTextView = (TextView) findViewById(R.id.movie_detail_title);
        mMovieSummaryTextView = (TextView) findViewById(R.id.movie_detail_summary);
        mMovieReleaseTextView = (TextView) findViewById(R.id.movie_detail_release_date);
        mMovieRatedTextView = (TextView) findViewById(R.id.movie_detail_vote_average);
        mMoviePosterView = (ImageView) findViewById(R.id.movie_detail_poster_image_view);
        noReviews = (TextView) findViewById(R.id.empty_reviews_view);
        mReviewRecycler = (RecyclerView) findViewById(R.id.reviews_list_view);


        // Get the intent from the grid item that was tapped
        Intent intent = getIntent();
        mCurrentMovieUri = intent.getData();

        ImageButton playTrailerButton = (ImageButton) findViewById(R.id.watch_icon_button);
        playTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri y = Uri.parse(MovieEntry.COLUMN_TRAILER);
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW, y);
                startActivity(trailerIntent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(MovieDetail.this);
        mReviewRecycler.setLayoutManager(layoutManager);
        mAdapter = new ReviewAdapter();
        mReviewRecycler.setAdapter(mAdapter);
        if (checkforDatabase()) {
            loadMovieData();
        } else {
            getLoaderManager().restartLoader(MOVIE_DETAIL_LOADER, null, this);

        }
    }

    /**
     * A method to load movie data in the background
     */
    private void loadMovieData() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //new MovieDetail.FetchTrailerData().execute();
            getLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);
        }
    }

    private boolean checkforDatabase() {
        boolean empty = true;
        MovieDatabaseHelper helper = new MovieDatabaseHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();
        String check = "SELECT COUNT(*) FROM movies";
        Cursor cursor = database.rawQuery(check, null);
        if (cursor != null && cursor.moveToFirst()) {
            empty = (cursor.getInt(0) == 0);
        }
        cursor.close();
        return empty;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case MOVIE_DETAIL_LOADER:
            return new CursorLoader(this,
                    mCurrentMovieUri,
                    MOVIE_PROJECTION,
                    null,
                    null,
                    null);
            default:
                throw new RuntimeException("Loader not implemented" + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null && cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int posterPathColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_POSTER);
            int summaryColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_SUMMARY);
            int releaseDateColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE);
            int movieIdColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID);
            int titleColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_TITLE);
            int popularityColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_POPULARITY);
            int voteAverageColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE);
            int trailerSetColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_TRAILER_SET);
            int favoriteColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_FAVORITE);
            int reviewSetColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_REVIEW_SET);

            String posterPath = cursor.getString(posterPathColumnIndex);
            String summary = cursor.getString(summaryColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            String releaseDate = cursor.getString(releaseDateColumnIndex);
            String rating = cursor.getString(voteAverageColumnIndex);
            mMovieId = cursor.getInt(movieIdColumnIndex);

            String released = getString(R.string.released_on_text) + " " + releaseDate;
            String rated = getString(R.string.rated_text) + " " + String.valueOf(rating);

            mMovieSummaryTextView.setText(summary);
            mMovieTitleTextView.setText(title);
            mMovieReleaseTextView.setText(released);
            mMovieRatedTextView.setText(rated);

            String baseImageUrl = "http://image.tmdb.org/t/p/w342/";

            Picasso.with(mMoviePosterView.getContext()).load(baseImageUrl + posterPath).into(mMoviePosterView);
        }
        if (mMovieId != 0) {
            new FetchReviewData().execute();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    // TODO place in it's own java file
    public class FetchTrailerData extends AsyncTask<URL, Void, ContentValues[]> {

        @Override
        protected ContentValues[] doInBackground(URL... params) {

            try {

                String responseFromHttp = NetworkUtility.getResponseFromHttp(mTrailerUrlSet);
                mTrailerResults = JsonUtility.getTrailerItemsFromJson(MovieDetail.this, responseFromHttp);

                return mTrailerResults;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public class FetchReviewData extends AsyncTask<URL, Void, ContentValues[]> {

        @Override
        protected ContentValues[] doInBackground(URL... params) {

            mReviewUrlSet = NetworkUtility.buildReviewDatasetUrl(String.valueOf(mMovieId));
            Log.v("MOVIEID", String.valueOf(mMovieId));
            try {
                String responseFromHttp = NetworkUtility.getResponseFromHttp(mReviewUrlSet);
                mReviewResults = JsonUtility.getJsonReviews(MovieDetail.this, responseFromHttp);

                Log.v("REVIEW RESULTS", mReviewResults.toString());
                return JsonUtility.getReviewItemsFromJson(MovieDetail.this, mReviewResults.toString());

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}