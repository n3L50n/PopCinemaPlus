package com.node_coyote.popcinema;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.node_coyote.popcinema.data.MovieContract;
import com.node_coyote.popcinema.utility.JsonUtility;
import com.node_coyote.popcinema.utility.NetworkUtility;
import com.node_coyote.popcinema.utility.Review;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity{

    public ContentValues[] mTrailerResults;
    public ContentValues[] mReviewResults;
    public URL mTrailerUrlSet;
    public URL mReviewUrlSet;
    public ReviewAdapter mAdapter;
    public TextView noReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mAdapter = new ReviewAdapter(this, new ArrayList<Review>());
        ListView listView = (ListView) findViewById(R.id.reviews_list_view);
        listView.setAdapter(mAdapter);

        // Find movie Detail TextViews
        TextView movieTitleTextView = (TextView) findViewById(R.id.movie_detail_title);
        TextView movieSummaryTextView = (TextView) findViewById(R.id.movie_detail_summary);
        TextView movieReleaseTextView = (TextView) findViewById(R.id.movie_detail_release_date);
        TextView movieRatedTextView = (TextView) findViewById(R.id.movie_detail_vote_average);
        ImageView moviePosterView = (ImageView) findViewById(R.id.movie_detail_poster_image_view);
        noReviews = (TextView) findViewById(R.id.empty_reviews_view);


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
                mReviewUrlSet = NetworkUtility.buildReviewDatasetUrl(movieId);

                new FetchTrailerData().execute(mTrailerUrlSet);
                new FetchReviewData().execute(mReviewUrlSet);

                ImageButton playTrailerButton = (ImageButton) findViewById(R.id.watch_icon_button);
                playTrailerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Uri y = Uri.parse(mTrailerResults.get(0));
                        Uri y = Uri.parse(MovieContract.MovieEntry.COLUMN_TRAILER);
                        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, y);
                        startActivity(trailerIntent);
                    }
                });
            }
        }
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
