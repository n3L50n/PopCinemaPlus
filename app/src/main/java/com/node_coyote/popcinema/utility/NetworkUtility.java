package com.node_coyote.popcinema.utility;

/**
 * Created by node_coyote on 5/1/17.
 */

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Utilities to communicate with themoviedb.org api
 */
public class NetworkUtility {

    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";

    // The "key" part of the appended query parameter for the api key
    private static final String API_KEY = "api_key=";

    // TODO REPLACE WITH API KEY FOR TESTING SO AS NOT TO COMMIT TO GITHUB
    // The "value" part of the appended query parameter for the api key
    private static final String API_KEY_VALUE = "REPLACE WITH API KEY FOR TESTING SO AS NOT TO COMMIT TO GITHUB";

    private static final String TOP_RATED = "top_rated";

    private static final String POPULAR = "popular";

    private static final String QUERY_PARAMETER = "?";

    public static URL buildPopularMovieUrl() {

        String u = MOVIE_BASE_URL
                + POPULAR
                + QUERY_PARAMETER
                + API_KEY
                + API_KEY_VALUE;

        URL popularMovieUrL = null;
        try {
            popularMovieUrL = new URL(u);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (popularMovieUrL != null) {
            Log.v("MOVIES", popularMovieUrL.toString());
        }
        return popularMovieUrL;
    }

    public static URL buildTopRatedMovieUrl() {

        String builtUrl = MOVIE_BASE_URL
                + TOP_RATED
                + QUERY_PARAMETER
                + API_KEY
                + API_KEY_VALUE;

        URL topRatedMovieUrL = null;
        try {
            topRatedMovieUrL = new URL(builtUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (topRatedMovieUrL != null) {
            Log.v("MOVIES", topRatedMovieUrL.toString());
        }
        return topRatedMovieUrL;
    }

    public static String getResponseFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
