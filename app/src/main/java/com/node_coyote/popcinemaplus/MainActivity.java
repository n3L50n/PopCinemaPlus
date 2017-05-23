package com.node_coyote.popcinemaplus;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.node_coyote.popcinemaplus.data.MovieContract.MovieEntry;
import com.node_coyote.popcinemaplus.data.MovieDatabaseHelper;
import com.node_coyote.popcinemaplus.utility.JsonUtility;
import com.node_coyote.popcinemaplus.utility.NetworkUtility;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * A variable to store the grids view recycler for the movie posters
     */
    private RecyclerView mRecyclerView;

    /**
     * An adapter to populate the grid movie posters with popular movie posters
     */
    private MovieAdapter mAdapter;

    /**
     * Use to display text if not connected to the internet
     */
    private TextView mEmptyDisplay;
    private TextView mEmptyDisplaySubtext;

    private ContentValues[] mMovieData;
    private int mPosition = RecyclerView.NO_POSITION;

    /**
     * Use as an arbitrary identifier for the movie loader data.
     */
    private static final int POPULAR_MOVIE_LOADER = 42;
    private static final int TOP_RATED_LOADER = 9;

    private static final int SORT_POPULAR_MOVIES = 0;
    private static final int SORT_TOP_RATED_MOVIES = 1;
    private static final int SORT_FAVORITES = 2;

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
        setContentView(R.layout.activity_main);

        mEmptyDisplay = (TextView) findViewById(R.id.empty_screen_display);
        mEmptyDisplay.setVisibility(View.VISIBLE);

        mEmptyDisplaySubtext = (TextView) findViewById(R.id.empty_screen_display_subtext);
        mEmptyDisplaySubtext.setVisibility(View.VISIBLE);

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        Context context = MainActivity.this;

        int numberOfMovieColumns = 2;

        //  Create a grid layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(context, numberOfMovieColumns);
        mRecyclerView.setLayoutManager(layoutManager);


        mAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mAdapter);

        getLoaderManager().initLoader(POPULAR_MOVIE_LOADER, null, MainActivity.this);
        loadMovieData();
    }

    /**
     * A method to load movie data in the background
     */
    private void loadMovieData() {
        new FetchMovieData().execute();
    }

    @Override
    public void onClick(String[] movieData) {

        // Where the click is coming from
        Context context = this;
        // Where the click is going
        Class destination = MovieDetail.class;
        // Create a new intent
        Intent intent = new Intent(context, destination);
        intent.putExtra(Intent.EXTRA_TEXT, movieData);
        startActivity(intent);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {
            case POPULAR_MOVIE_LOADER:
                return new CursorLoader(this,
                        MovieEntry.CONTENT_URI,
                        MOVIE_PROJECTION,
                        null,
                        null,
                        null);

            case TOP_RATED_LOADER:
                String selection = MovieEntry.COLUMN_VOTE_AVERAGE;
                return new CursorLoader(this,
                        MovieEntry.CONTENT_URI,
                        MOVIE_PROJECTION,
                        selection,
                        null,
                        null);
            default:
                throw new RuntimeException("Laoder not implemented" + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        //if (data.getCount() != 0) showMoviesGrid();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_by, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_popular:
                // Sort by popularity
                sort(SORT_POPULAR_MOVIES);
                return true;
            case R.id.action_top_rated:
                // Sort by top rated
                sort(SORT_TOP_RATED_MOVIES);
                return true;
            case R.id.action_favorites:
                // Sort by favorites
                sort(SORT_FAVORITES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sort(int parameter){

        String sortOrder = null;
        switch (parameter) {
            case SORT_POPULAR_MOVIES:
                //query() popular
                sortOrder = MovieEntry.COLUMN_POPULARITY + " ASC";
                break;
            case SORT_TOP_RATED_MOVIES:
                // query() top rated
                sortOrder = MovieEntry.COLUMN_VOTE_AVERAGE + " ASC";
                break;
            case SORT_FAVORITES:
                // query() favorites
                // TODO  if a movie has a 1 in the favorite column, show it.
                sortOrder = MovieEntry.COLUMN_FAVORITE;
                break;
        }
        MovieDatabaseHelper helper = new MovieDatabaseHelper(getApplicationContext());
        mAdapter.swapCursor(helper.query(MOVIE_PROJECTION, sortOrder));
    }

    /**
     * Class to help move the data request off the main thread
     */
    public class FetchMovieData extends AsyncTask<String, Void, ContentValues[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ContentValues[] doInBackground(String... params) {

            URL popularMovieUrl = NetworkUtility.buildPopularMovieUrl();

            try {
                // By default, the app opens with Popular Movies. It is up to the user to toggle to top rated
                String jsonPopularMovieResponse = NetworkUtility.getResponseFromHttp(popularMovieUrl);
                mMovieData = JsonUtility.getMovieStringsFromJson(MainActivity.this, jsonPopularMovieResponse);

                return mMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ContentValues[] movieData) {

            if (movieData != null) {
                //mAdapter.swapCursor(movieData);
                mAdapter.setMovieData(movieData);
                mEmptyDisplay.setVisibility(View.INVISIBLE);
                mEmptyDisplaySubtext.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                new FetchTopRatedMovieData().execute();
                //loadExtras();
            }
        }
    }

    /**
     * Class to help move the data request off the main thread
     */
    public class FetchTopRatedMovieData extends AsyncTask<String, Void, ContentValues[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ContentValues[] doInBackground(String... params) {

            URL topRatedMovieUrl = NetworkUtility.buildTopRatedMovieUrl();

            try {
                // By default, the app opens with Popular Movies. It is up to the user to toggle to top rated
                String jsonTopRatedResponse = NetworkUtility.getResponseFromHttp(topRatedMovieUrl);
                mMovieData = JsonUtility.getMovieStringsFromJson(MainActivity.this, jsonTopRatedResponse);
                return mMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ContentValues[] topRatedMovieData) {

            if (topRatedMovieData != null) {
                //mAdapter.swapCursor(topRatedMovieData);
                mAdapter.setMovieData(topRatedMovieData);
            }
        }
    }

    /**
     * Class to help move the trailers and reviews request off the main thread
     */
    public class FetchTrailerReviewData extends AsyncTask<String, Void, ContentValues[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ContentValues[] doInBackground(String... params) {

            URL movieTrailersUrl = NetworkUtility.buildVideoDatasetUrl(MovieEntry.COLUMN_MOVIE_ID);
            URL movieReviewsUrl = NetworkUtility.buildReviewDatasetUrl(MovieEntry.COLUMN_MOVIE_ID);

            try {
                String jsonTrailerResponse = NetworkUtility.getResponseFromHttp(movieTrailersUrl);
                String jsonReviewResponse = NetworkUtility.getResponseFromHttp(movieReviewsUrl);
                mMovieData = JsonUtility.getTrailerItemsFromJson(MainActivity.this, jsonTrailerResponse);
                mMovieData = JsonUtility.getReviewItemsFromJson(MainActivity.this, jsonReviewResponse);

                return mMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private void loadExtras() {
        new FetchTrailerReviewData().execute();
    }

}
