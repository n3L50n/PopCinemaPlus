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

public final class MovieJsonUtility {

    public static List<Movie> getMovieStringsFromJson(Context context, String movieJsonString) throws JSONException {

        // results is the array holding all of our movies.
        final String MOVIE_RESULTS = "results";

        // movie objects within the results array
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String TITLE = "original_title";
        final String TOP_RATED = "vote_average";
        final String RELEASE_DATE = "release_date";

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

                Movie newMovie = new Movie(poster, summary, title, release, topRated);
                parsedMovies.add(newMovie);
            }

        } catch (JSONException e) {
            Log.e("MovieJSONUtility", "Problem parsing movie JSON results");
        }

        return parsedMovies;
    }

}
