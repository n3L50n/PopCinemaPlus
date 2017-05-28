package com.node_coyote.popcinemaplus;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private Button mRetryButton;

    private ContentValues[] mMovieData;
    private int mPosition = RecyclerView.NO_POSITION;

    /**
     * Use as an arbitrary identifier for the movie loader data.
     */
    private static final int MOVIE_LOADER = 42;
    private String SORT_KEY = "Current Sort";
    private int CURRENT_SORT;

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
            MovieEntry.COLUMN_FAVORITE,
            MovieEntry.COLUMN_REVIEW_SET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            CURRENT_SORT = savedInstanceState.getInt(SORT_KEY);
        }

        mEmptyDisplay = (TextView) findViewById(R.id.empty_screen_display);
        mEmptyDisplaySubtext = (TextView) findViewById(R.id.empty_screen_display_subtext);
        mRetryButton = (Button) findViewById(R.id.retry_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        int numberOfMovieColumns = 2;

        if (checkForDatabase()) {
            loadMovieData();
        } else {
            getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
        }

        //  Create a grid layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, numberOfMovieColumns);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        }

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMovieData();
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
            new FetchMovieData().execute();
            new FetchTopRatedMovieData().execute();
            getLoaderManager().initLoader(MOVIE_LOADER, null, this);
            showMovies();
        } else {
            showLoadingIndicator();
        }
    }

    /**
     * Helper method to bundle up view toggling.
     * This one dismisses the items that show when there is no internet and replaces them with our roster.
     */
    private void showMovies() {
        mEmptyDisplay.setVisibility(View.INVISIBLE);
        mEmptyDisplaySubtext.setVisibility(View.INVISIBLE);
        mRetryButton.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Helper method to bundle up view toggling.
     * This one informs our users that there is no internet and hides the empty roster.
     */
    private void showLoadingIndicator() {
        mEmptyDisplay.setVisibility(View.VISIBLE);
        mEmptyDisplaySubtext.setVisibility(View.VISIBLE);
        mRetryButton.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * This method helps us check if our database is empty.
     *
     * @return True if the database is empty and false if it exists.
     */
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

    @Override
    public void onClick(Uri movieDataUri) {

        // Where the click is coming from
        Context context = this;
        // Where the click is going
        Class destination = MovieDetail.class;
        // Create a new intent
        Intent intent = new Intent(context, destination);
        intent.setData(movieDataUri);
        startActivity(intent);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {
            case MOVIE_LOADER:
                return new CursorLoader(this,
                        MovieEntry.CONTENT_URI,
                        MOVIE_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader not implemented" + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (cursor.getCount() != 0) showMovies();
        sort(CURRENT_SORT);
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

    private void sort(int parameter) {

        MovieDatabaseHelper helper = new MovieDatabaseHelper(getApplicationContext());

        String sortOrder;
        switch (parameter) {
            case SORT_POPULAR_MOVIES:
                CURRENT_SORT = SORT_POPULAR_MOVIES;
                //query() popular
                sortOrder = MovieEntry.COLUMN_POPULARITY + " DESC";
                mAdapter.swapCursor(helper.getSortOrder(MOVIE_PROJECTION, sortOrder));
                break;
            case SORT_TOP_RATED_MOVIES:
                CURRENT_SORT = SORT_TOP_RATED_MOVIES;
                // query() top rated
                sortOrder = MovieEntry.COLUMN_VOTE_AVERAGE + " DESC";
                mAdapter.swapCursor(helper.getSortOrder(MOVIE_PROJECTION, sortOrder));
                break;
            case SORT_FAVORITES:
                CURRENT_SORT = SORT_FAVORITES;
                // query() favorites
                String selection = "favorite = 1";
                sortOrder = MovieEntry.COLUMN_FAVORITE + " DESC";
                mAdapter.swapCursor(getContentResolver().query(MovieEntry.CONTENT_URI, MOVIE_PROJECTION, selection, null, sortOrder));
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SORT_KEY, CURRENT_SORT);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        CURRENT_SORT = savedInstanceState.getInt(SORT_KEY);
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
                // By default, the app opens with Popular Movies. It is up to the user to toggle to top rated or favorites.
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
                mAdapter.setMovieData(movieData);
                showMovies();
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
                // By default, the app opens with Popular Movies. It is up to the user to toggle to top rated or favorites.
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
                mAdapter.setMovieData(topRatedMovieData);
            }
        }
    }
}
