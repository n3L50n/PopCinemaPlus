package com.node_coyote.popcinema;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        //  Create a grid layout manager
        Context context = MainActivity.this;

        // TODO user can set columns dynamically in menu. action_columns
        int numberOfMovieColumns = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(context, numberOfMovieColumns);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MovieAdapter();

        mRecyclerView.setAdapter(mAdapter);

        loadMovieDate();
    }

    /**
     * A method to load movie data in the background
     */
    private void loadMovieDate() {
        
        new FetchMovieData().execute();
    }

    public class FetchMovieData extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(String... params) {
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
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
