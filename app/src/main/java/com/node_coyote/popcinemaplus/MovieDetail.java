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
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.node_coyote.popcinemaplus.data.MovieContract.MovieEntry;
import com.node_coyote.popcinemaplus.data.MovieDatabaseHelper;
import com.node_coyote.popcinemaplus.data.Review;
import com.node_coyote.popcinemaplus.utility.JsonUtility;
import com.node_coyote.popcinemaplus.utility.NetworkUtility;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetail extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public ArrayList<String> mTrailerResults;
    public ArrayList<Review> mReviews;
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
    private int mMovieId;
    private boolean mFavoritesClicked;
    private ImageButton mFavoritesButton;

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
            MovieEntry.COLUMN_REVIEW_SET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Get the intent from the grid item that was tapped
        Intent intent = getIntent();
        mCurrentMovieUri = intent.getData();
        mFavoritesButton = (ImageButton) findViewById(R.id.favorite_icon_button);

        // Find movie Detail TextViews
        mMovieTitleTextView = (TextView) findViewById(R.id.movie_detail_title);
        mMovieSummaryTextView = (TextView) findViewById(R.id.movie_detail_summary);
        mMovieReleaseTextView = (TextView) findViewById(R.id.movie_detail_release_date);
        mMovieRatedTextView = (TextView) findViewById(R.id.movie_detail_vote_average);
        mMoviePosterView = (ImageView) findViewById(R.id.movie_detail_poster_image_view);
        noReviews = (TextView) findViewById(R.id.empty_reviews_view);
        ListView reviewList = (ListView) findViewById(R.id.reviews_list_view);

        mReviews = new ArrayList<>();
        mAdapter = new ReviewAdapter(this, mReviews);
        reviewList.setAdapter(mAdapter);

        if (checkForDatabase()) {
            loadMovieData();
        } else {
            getLoaderManager().restartLoader(MOVIE_DETAIL_LOADER, null, this);
        }

        ImageButton playTrailerButton = (ImageButton) findViewById(R.id.watch_icon_button);
        playTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri firstTrailerResult = Uri.parse(mTrailerResults.get(0));
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW, firstTrailerResult);
                startActivity(trailerIntent);
            }
        });
        mFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mFavoritesClicked) {
                    favorite(1);
                    mFavoritesButton.setImageResource(R.drawable.ic_favorite_fill);
                    mFavoritesClicked = false;

                } else {
                    favorite(0);
                    mFavoritesButton.setImageResource(R.drawable.ic_favorite_border);
                    mFavoritesClicked = true;
                }
            }
        });
    }

    /**
     * A method to load movie data in the background
     */
    private void loadMovieData() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);
        }
    }

    private boolean checkForDatabase() {
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

    private void favorite(int parameter) {
        ContentValues contentValues = new ContentValues();

        switch (parameter) {
            case 0:
                // Update to 0 False.
                contentValues.put(MovieEntry.COLUMN_FAVORITE, 0);
                getContentResolver().update(mCurrentMovieUri, contentValues, null, null);
                break;
            case 1:
                // Update to 1 True.
                contentValues.put(MovieEntry.COLUMN_FAVORITE, 1);
                getContentResolver().update(mCurrentMovieUri, contentValues, null, null);
                break;
            default:
                throw new RuntimeException("Invalid favorites parameter");

        }
    }

    private void favoriteCheck(int isFavorite, String[] projection){
        switch (isFavorite) {
            case 0:
                Log.v("Fvr", String.valueOf(isFavorite));
                getContentResolver().query(mCurrentMovieUri, projection, mCurrentMovieUri.toString().substring(51), null, null );
                mFavoritesButton.setImageResource(R.drawable.ic_favorite_border);
                break;
            case 1:
                Log.v("Fvr", String.valueOf(isFavorite));
                getContentResolver().query(mCurrentMovieUri, projection, mCurrentMovieUri.toString().substring(51), null, null );
                mFavoritesButton.setImageResource(R.drawable.ic_favorite_fill);

                break;
            default:
                Log.v("Fvr", String.valueOf(isFavorite));
                getContentResolver().query(mCurrentMovieUri, projection, mCurrentMovieUri.toString().substring(51), null, null );
                mFavoritesButton.setImageResource(R.drawable.ic_favorite_border);
                break;
        }
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
            int voteAverageColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE);
            int favoriteColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_FAVORITE);

            String posterPath = cursor.getString(posterPathColumnIndex);
            String summary = cursor.getString(summaryColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            String releaseDate = cursor.getString(releaseDateColumnIndex);
            String rating = cursor.getString(voteAverageColumnIndex);
            mMovieId = cursor.getInt(movieIdColumnIndex);

            // Check if item is a favorite, update UI accordingly.
            int isFavorite = cursor.getInt(favoriteColumnIndex);
            String[] projection = new String[]{"id, favorite", mCurrentMovieUri.toString().substring(51)};
            favoriteCheck(isFavorite, projection);
            String released = getString(R.string.released_on_text) + " " + releaseDate;
            String rated = getString(R.string.rated_text) + " " + String.valueOf(rating);

            // Update views with text and poster image.
            mMovieSummaryTextView.setText(summary);
            mMovieTitleTextView.setText(title);
            mMovieReleaseTextView.setText(released);
            mMovieRatedTextView.setText(rated);
            String baseImageUrl = "http://image.tmdb.org/t/p/w342/";
            Picasso.with(mMoviePosterView.getContext()).load(baseImageUrl + posterPath).into(mMoviePosterView);
        }

        new FetchReviewData().execute();
        new FetchTrailerData().execute();
        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.clear();
    }

    public class FetchTrailerData extends AsyncTask<URL, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(URL... params) {
            mTrailerUrlSet = NetworkUtility.buildVideoDatasetUrl(String.valueOf(mMovieId));

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

    public class FetchReviewData extends AsyncTask<URL, Void, List<Review>> {

        @Override
        protected List<Review> doInBackground(URL... params) {
            mReviewUrlSet = NetworkUtility.buildReviewDatasetUrl(String.valueOf(mMovieId));
            try {
                String responseFromHttp = NetworkUtility.getResponseFromHttp(mReviewUrlSet);
                mReviews = JsonUtility.getReviewItemsFromJson(responseFromHttp);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mReviews != null && !mReviews.isEmpty()) {
                            mAdapter.addAll(mReviews);
                            noReviews.setVisibility(View.GONE);
                        } else {
                            noReviews.setVisibility(View.VISIBLE);
                        }
                    }
                });
                return mReviews;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }
}