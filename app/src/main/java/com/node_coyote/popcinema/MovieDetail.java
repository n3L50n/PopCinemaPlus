package com.node_coyote.popcinema;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MovieDetail extends AppCompatActivity {

    private String mMovieTitle;
    private TextView mMovieTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Find Movie Detail TextViews
        mMovieTitleTextView = (TextView) findViewById(R.id.movie_detail_title);

        // Get the intent from the grid item that was tapped
        Intent intent = getIntent();

        // If it's not empty, check if it has extra items. If so, set the text views and image view
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                mMovieTitle = intent.getStringExtra(Intent.EXTRA_TEXT);
                mMovieTitleTextView.setText(mMovieTitle);
            }
        }
    }
}
