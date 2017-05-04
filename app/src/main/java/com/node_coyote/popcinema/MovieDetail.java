package com.node_coyote.popcinema;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    private TextView mMovieTitleTextView;
    private TextView mMovieSummaryTextView;
    private TextView mMovieReleaseTextView;
    //TODO fx all this
    private TextView mMovieRatedTextView;
    private ImageView mMoviePosterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Find Movie Detail TextViews
        mMovieTitleTextView = (TextView) findViewById(R.id.movie_detail_title);
        mMovieSummaryTextView = (TextView) findViewById(R.id.movie_detail_summary);
        mMovieReleaseTextView = (TextView) findViewById(R.id.movie_detail_release_date);
        mMovieRatedTextView = (TextView) findViewById(R.id.movie_detail_vote_average);
        mMoviePosterView = (ImageView) findViewById(R.id.movie_detail_poster_image_view);


        // Get the intent from the grid item that was tapped
        Intent intent = getIntent();

        // If it's not empty, check if it has extra items. If so, set the text views and image view
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                String movieTitle = intent.getStringArrayExtra(Intent.EXTRA_TEXT)[0];
                String movieSummary = intent.getStringArrayExtra(Intent.EXTRA_TEXT)[1];
                String movieRelease = intent.getStringArrayExtra(Intent.EXTRA_TEXT)[2];
                String moviePoster = intent.getStringArrayExtra(Intent.EXTRA_TEXT)[3];
                mMovieTitleTextView.setText(movieTitle);
                mMovieSummaryTextView.setText(movieSummary);
                mMovieReleaseTextView.setText(movieRelease);
                String BASE = "http://image.tmdb.org/t/p/w342/";

                Picasso.with(mMoviePosterView.getContext()).load( BASE + moviePoster).into(mMoviePosterView);
            }
        }
    }
}
