package com.node_coyote.popcinemaplus;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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
import com.node_coyote.popcinemaplus.utility.JsonUtility;
import com.node_coyote.popcinemaplus.utility.NetworkUtility;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public ContentValues[] mTrailerResults;
    public ContentValues[] mReviewResults;
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
            MovieEntry.COLUMN_FAVORITE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

       // mAdapter = new ReviewAdapter(this, new ArrayList<Review>());
        ListView listView = (ListView) findViewById(R.id.reviews_list_view);
        //listView.setAdapter(mAdapter);

        // Find movie Detail TextViews
        mMovieTitleTextView = (TextView) findViewById(R.id.movie_detail_title);
        mMovieSummaryTextView = (TextView) findViewById(R.id.movie_detail_summary);
        mMovieReleaseTextView = (TextView) findViewById(R.id.movie_detail_release_date);
        mMovieRatedTextView = (TextView) findViewById(R.id.movie_detail_vote_average);
        mMoviePosterView = (ImageView) findViewById(R.id.movie_detail_poster_image_view);
        noReviews = (TextView) findViewById(R.id.empty_reviews_view);


        // Get the intent from the grid item that was tapped
        Intent intent = getIntent();
        mCurrentMovieUri = intent.getData();
        getLoaderManager().initLoader(1, null, this);

        // If it's not empty, check if it has extra items. If so, set the text views and image view
//        if (intent != null) {
//            if (intent.hasExtra(Intent.EXTRA_TEXT)) {

                /**
                String movieTitle = intent.getStringArrayExtra(Intent.EXTRA_TEXT)[0];
                String movieSummary = intent.getStringArrayExtra(Intent.EXTRA_TEXT)[1];
                String movieRelease = intent.getStringArrayExtra(Intent.EXTRA_TEXT)[2];
                String moviePoster = intent.getStringArrayExtra(Intent.EXTRA_TEXT)[3];
                String movieRating = intent.getStringArrayExtra(Intent.EXTRA_TEXT)[4];
                String movieId = intent.getStringArrayExtra(Intent.EXTRA_TEXT)[5];
                String release = getString(R.string.released_on_text) + " " + movieRelease;
                String rated = getString(R.string.rated_text) + " " + movieRating;

                movieTitleTextView.setText(movieTitle);
                movieSummaryTextView.setText(movieSummary);
                movieReleaseTextView.setText(release);
                movieRatedTextView.setText(rated);

                String baseImageUrl = "http://image.tmdb.org/t/p/w342/";

                Picasso.with(moviePosterView.getContext()).load(baseImageUrl + moviePoster).into(moviePosterView);

                mTrailerUrlSet = NetworkUtility.buildVideoDatasetUrl(movieId);
                mReviewUrlSet = NetworkUtility.buildReviewDatasetUrl(movieId);

                new FetchTrailerData().execute(mTrailerUrlSet);
                new FetchReviewData().execute(mReviewUrlSet);
                 **/

                ImageButton playTrailerButton = (ImageButton) findViewById(R.id.watch_icon_button);
                playTrailerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri y = Uri.parse(MovieEntry.COLUMN_TRAILER);
                        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, y);
                        startActivity(trailerIntent);
                    }
                });
           // }
        }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                mCurrentMovieUri,
                MOVIE_PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null && cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()){
            int posterPathColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_POSTER);
            int summaryColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_SUMMARY);
            int releaseDateColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE);
            int movieIdColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID);
            int titleColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_TITLE);
            int popularityColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_POPULARITY);
            int voteAverageColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE);
            int trailerSetColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_TRAILER_SET);
            int favoriteColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_FAVORITE);

            String posterPath = cursor.getString(posterPathColumnIndex);
            String summary = cursor.getString(summaryColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            String releaseDate = cursor.getString(releaseDateColumnIndex);
            String rating = cursor.getString(voteAverageColumnIndex);
            int movieId = cursor.getInt(movieIdColumnIndex);

            String released = getString(R.string.released_on_text) + " " + releaseDate;
            String rated = getString(R.string.rated_text) + " " + String.valueOf(rating);

            mMovieSummaryTextView.setText(summary);
            mMovieTitleTextView.setText(title);
            mMovieReleaseTextView.setText(released);
            mMovieRatedTextView.setText(rated);

            String baseImageUrl = "http://image.tmdb.org/t/p/w342/";

            Picasso.with(mMoviePosterView.getContext()).load(baseImageUrl + posterPath).into(mMoviePosterView);

            //TODO Refactor ReviewAdapter
            mTrailerUrlSet = NetworkUtility.buildVideoDatasetUrl(String.valueOf(movieId));
            mReviewUrlSet = NetworkUtility.buildReviewDatasetUrl(String.valueOf(movieId));

            new FetchTrailerData().execute(mTrailerUrlSet);
            new FetchReviewData().execute(mReviewUrlSet);

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

                String responseFromHttp =  NetworkUtility.getResponseFromHttp(mTrailerUrlSet);
                mTrailerResults = JsonUtility.getTrailerItemsFromJson(MovieDetail.this , responseFromHttp);

                return mTrailerResults;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    // TODO place in it's own java file
    public class FetchReviewData extends AsyncTask<URL, Void, ContentValues[]> {

        @Override
        protected ContentValues[] doInBackground(URL... params) {

            try {
                String responseFromHttp =  NetworkUtility.getResponseFromHttp(mReviewUrlSet);
                mReviewResults = JsonUtility.getReviewItemsFromJson(MovieDetail.this, responseFromHttp);

                return mReviewResults;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}