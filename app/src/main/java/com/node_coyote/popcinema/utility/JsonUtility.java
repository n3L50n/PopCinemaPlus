package com.node_coyote.popcinema.utility;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.node_coyote.popcinema.data.MovieContract.MovieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by node_coyote on 5/2/17.
 */

public final class JsonUtility {

    public static ContentValues[] getMovieStringsFromJson(Context context, String movieJsonString) throws JSONException {

        // results is the array holding all of our movies.
        final String MOVIE_RESULTS = "results";

        // movie objects within the results array
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String TITLE = "original_title";
        final String TOP_RATED = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_ID = "id";

        JSONObject root = new JSONObject(movieJsonString);
        JSONArray results = root.getJSONArray(MOVIE_RESULTS);
        ContentValues[] parsedMovieValues = new ContentValues[root.length()];

        try {

            for (int i = 0; i < results.length(); i++) {

                JSONObject movie = results.getJSONObject(i);

                String poster = movie.getString(POSTER_PATH);
                String summary = movie.getString(OVERVIEW);
                String title = movie.getString(TITLE);
                String release = movie.getString(RELEASE_DATE);
                double topRated = movie.getDouble(TOP_RATED);
                long movieId = movie.getLong(MOVIE_ID);

                ContentValues values = new ContentValues();
                values.put(MovieEntry.COLUMN_POSTER, poster);
                values.put(MovieEntry.COLUMN_SUMMARY, summary);
                values.put(MovieEntry.COLUMN_TITLE, title);
                values.put(MovieEntry.COLUMN_RELEASE_DATE, release);
                values.put(MovieEntry.COLUMN_RATING, topRated);
                values.put(MovieEntry.COLUMN_MOVIE_ID, movieId);

                parsedMovieValues[i] = values;
            }

        } catch (JSONException e) {
            Log.e("MovieJSONUtility", "Problem parsing movie JSON results");
        }

        context.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, parsedMovieValues);
        return parsedMovieValues;
    }

    public static ContentValues[] getTrailerItemsFromJson(Context context, String trailerJsonString) throws JSONException {

        final String TRAILER_RESULTS = "results";

        // objects within the trailer results array
        final String TRAILER_KEY = "key";
        final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
        JSONObject root = new JSONObject(trailerJsonString);
        JSONArray results = root.getJSONArray(TRAILER_RESULTS);

        ContentValues[] parsedTrailerValues = new ContentValues[root.length()];

        try {

            for (int i = 0; i < results.length(); i ++) {

                JSONObject trailer = results.getJSONObject(i);

                String key = trailer.getString(TRAILER_KEY);
                String newTrailer = YOUTUBE_BASE_URL + key;

                ContentValues values = new ContentValues();
                values.put(MovieEntry.COLUMN_TRAILER, newTrailer);
                parsedTrailerValues[i] = values;
            }
        } catch (JSONException e) {
            Log.e("TrailerJSONUtility", "Problem parsing trailer json.");
        }

        context.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, parsedTrailerValues);
        return parsedTrailerValues;

    }

    public static ContentValues[] getReviewItemsFromJson(Context context , String trailerJsonString) throws JSONException {

        final String REVIEW_RESULTS = "results";

        // objects within the trailer results array
        final String AUTHOR = "author";
        final String CONTENT= "content";

        JSONObject root = new JSONObject(trailerJsonString);
        JSONArray results = root.getJSONArray(REVIEW_RESULTS);

        ContentValues[] parsedReviewValues = new ContentValues[results.length()];

        try {

            for (int i = 0; i < results.length(); i ++) {

                JSONObject review = results.getJSONObject(i);

                String author = review.getString(AUTHOR);
                String content = review.getString(CONTENT);

                ContentValues values = new ContentValues();

            }
        } catch (JSONException e) {
            Log.e("ReviewJSONUtility", "Problem parsing Reviews");
        }

        context.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, parsedReviewValues);
        return parsedReviewValues;

    }

}
