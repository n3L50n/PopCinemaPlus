package com.node_coyote.popcinema.utility;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by node_coyote on 5/2/17.
 */

public final class JsonUtility {

    public static List<Movie> getMovieStringsFromJson(Context context, String movieJsonString) throws JSONException {

        // results is the array holding all of our movies.
        final String MOVIE_RESULTS = "results";

        // movie objects within the results array
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String TITLE = "original_title";
        final String TOP_RATED = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_ID = "id";

        List<Movie> parsedMovies = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(movieJsonString);

            JSONArray results = root.getJSONArray(MOVIE_RESULTS);

            for (int i = 0; i < results.length(); i++) {

                JSONObject movie = results.getJSONObject(i);

                String poster = movie.getString(POSTER_PATH);
                String summary = movie.getString(OVERVIEW);
                String title = movie.getString(TITLE);
                String release = movie.getString(RELEASE_DATE);
                double topRated = movie.getDouble(TOP_RATED);
                long movieId = movie.getLong(MOVIE_ID);

                Movie newMovie = new Movie(poster, summary, title, release, topRated, movieId);
                parsedMovies.add(newMovie);
            }

        } catch (JSONException e) {
            Log.e("MovieJSONUtility", "Problem parsing movie JSON results");
        }

        return parsedMovies;
    }

    public static List<String> getTrailerItemsFromJson(Context context, String trailerJsonString) throws JSONException {

        final String TRAILER_RESULTS = "results";

        // objects within the trailer results array
        final String TRAILER_KEY = "key";
        final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

        List<String> parsedTrailer = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(trailerJsonString);
            JSONArray results = root.getJSONArray(TRAILER_RESULTS);

            for (int i = 0; i < results.length(); i ++) {

                JSONObject trailer = results.getJSONObject(i);

                String key = trailer.getString(TRAILER_KEY);
                String newTrailer = YOUTUBE_BASE_URL + key;
                parsedTrailer.add(newTrailer);
            }
        } catch (JSONException e) {
            Log.e("TrailerJSONUtility", parsedTrailer.toString());
        }

        return parsedTrailer;

    }

    public static ArrayList<Review> getReviewItemsFromJson(Context context , String trailerJsonString) throws JSONException {

        final String REVIEW_RESULTS = "results";

        // objects within the trailer results array
        final String AUTHOR = "author";
        final String CONTENT= "content";

        ArrayList<Review> parsedReview = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(trailerJsonString);
            JSONArray results = root.getJSONArray(REVIEW_RESULTS);

            for (int i = 0; i < results.length(); i ++) {

                JSONObject review = results.getJSONObject(i);

                String author = review.getString(AUTHOR);
                String content = review.getString(CONTENT);

                Review newReview = new Review(author, content);
                parsedReview.add(newReview);
            }
        } catch (JSONException e) {
            Log.e("ReviewJSONUtility", parsedReview.toString());
        }

        return parsedReview;

    }

}
