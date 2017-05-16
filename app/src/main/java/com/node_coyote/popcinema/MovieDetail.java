package com.node_coyote.popcinema;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.node_coyote.popcinema.utility.JsonUtility;
import com.node_coyote.popcinema.utility.NetworkUtility;
import com.node_coyote.popcinema.utility.Review;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetail extends AppCompatActivity{

    public List<String> mTrailerResults;
    public ArrayList<Review> mReviewResults;
    public URL mTrailerUrlSet;
    public URL mReviewUrlSet;
    public ReviewAdapter mAdapter;

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
                mReviewUrlSet = NetworkUtility.buildReviewDatasetUrl(movieId);
                test.setText(mReviewUrlSet.toString());

                new FetchTrailerData().execute(mTrailerUrlSet);
                new FetchReviewData().execute(mReviewUrlSet);

                ImageButton playTrailerButton = (ImageButton) findViewById(R.id.watch_icon_button);
                playTrailerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri y = Uri.parse(mTrailerResults.get(0));
                        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, y);
                        startActivity(trailerIntent);
                    }
                });


                if (mReviewResults != null && !mReviewResults.isEmpty()) {
                    mAdapter = new ReviewAdapter(this, mReviewResults);
                    ListView listView = (ListView) findViewById(R.id.reviews_list_view);
                    listView.setAdapter(mAdapter);
                    mAdapter.addAll(mReviewResults);

                }
            }
        }
    }


    // TODO place in it's own java file
    public class FetchTrailerData extends AsyncTask<URL, Void, List<String>> {

        @Override
        protected List<String> doInBackground(URL... params) {

            try {

                String l =  NetworkUtility.getResponseFromHttp(mTrailerUrlSet);
                mTrailerResults = JsonUtility.getTrailerItemsFromJson(MovieDetail.this , l);
                return mTrailerResults;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    // TODO place in it's own java file
    public class FetchReviewData extends AsyncTask<URL, Void, List<Review>> {

        @Override
        protected List<Review> doInBackground(URL... params) {

            try {
                String l =  NetworkUtility.getResponseFromHttp(mReviewUrlSet);
                mReviewResults = JsonUtility.getReviewItemsFromJson(l);
                return mReviewResults;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
