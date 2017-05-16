package com.node_coyote.popcinema;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.node_coyote.popcinema.utility.Movie;
import com.node_coyote.popcinema.utility.MovieJsonUtility;
import com.node_coyote.popcinema.utility.NetworkUtility;
import com.node_coyote.popcinema.utility.TrailerJsonUtility;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MovieDetail extends AppCompatActivity {

    public List<String> mTrailerResults;
    public URL mTrailerUrlSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Find Movie Detail TextViews
        TextView movieTitleTextView = (TextView) findViewById(R.id.movie_detail_title);
        TextView movieSummaryTextView = (TextView) findViewById(R.id.movie_detail_summary);
        TextView movieReleaseTextView = (TextView) findViewById(R.id.movie_detail_release_date);
        TextView movieRatedTextView = (TextView) findViewById(R.id.movie_detail_vote_average);
        ImageView moviePosterView = (ImageView) findViewById(R.id.movie_detail_poster_image_view);

        // TODO remove after trailer and reviews are linked up
        TextView test =(TextView) findViewById(R.id.test_reviews_label_text_view);

        // Get the intent from the grid item that was tapped
        Intent intent = getIntent();

        // If it's not empty, check if it has extra items. If so, set the text views and image view
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
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

                new FetchTrailerData().execute(mTrailerUrlSet);

                ImageButton playTrailerButton = (ImageButton) findViewById(R.id.watch_icon_button);
                playTrailerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri y = Uri.parse(mTrailerResults.get(0));
                        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, y);
                        startActivity(trailerIntent);
                    }
                });
            }
        }
    }


    // TODO place in it's own java file
    public class FetchTrailerData extends AsyncTask<URL, Void, List<String>> {

        @Override
        protected List<String> doInBackground(URL... params) {

            try {

                String l =  NetworkUtility.getResponseFromHttp(mTrailerUrlSet);
                mTrailerResults = TrailerJsonUtility.getTrailerItemsFromJson(MovieDetail.this , l);
                return mTrailerResults;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
