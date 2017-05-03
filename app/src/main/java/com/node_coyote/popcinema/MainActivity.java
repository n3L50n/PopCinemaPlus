package com.node_coyote.popcinema;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.node_coyote.popcinema.utility.Movie;
import com.node_coyote.popcinema.utility.MovieJsonUtility;
import com.node_coyote.popcinema.utility.NetworkUtility;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    /**
     * A variable to store the grids view recycler for the movie posters
     */
    private RecyclerView mRecyclerView;

    /**
     * An adapter to populate the grid movie posters with popular movie posters
     */
    private MovieAdapter mAdapter;

    /**
     * An error to display if not connected to the internet
     */
    private TextView mErrorDisplay;

    private boolean mMovieDataLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieDataLoaded = false;

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        //  Create a grid layout manager
        Context context = MainActivity.this;

        // TODO user can set columns dynamically in menu. action_columns
        int numberOfMovieColumns = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(context, numberOfMovieColumns);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mAdapter);

        loadMovieData();
    }

    /**
     * A method to load movie data in the background
     */
    private void loadMovieData() {
        new FetchMovieData().execute();
    }

    @Override
    public void onClick(String movieData) {

        // Where the click is coming from
        Context context = this;
        // Where the click is going
        Class destination = MovieDetail.class;
        // Create a new intent
        Intent intent = new Intent(context, destination);
        startActivity(intent);

    }

    public class FetchMovieData extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            URL popularMovieUrl = NetworkUtility.buildPopularMovieUrl();

            try {
                // By default, the app opens with Popular Movies. It is up to the user to toggle to top rated
                String jsonPopularMovieResponse = NetworkUtility.getResponseFromHttp(popularMovieUrl);

                List<Movie> data = MovieJsonUtility.getMovieStringsFromJson(MainActivity.this, jsonPopularMovieResponse);

                return data;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movieData) {

            if (movieData != null) {
                mAdapter.setMovieData(movieData);
                mMovieDataLoaded = true;
            }
        }
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
                return true;
            case R.id.action_top_rated:
                // Sort by top rated
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
