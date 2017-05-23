package com.node_coyote.popcinemaplus.utility;

/**
 * Created by node_coyote on 5/1/17.
 */

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

    // The base url to fetch movies from.
    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";

    // The "key" part of the appended query parameter for the api key
    private static final String API_KEY = "api_key=";

    // The "value" part of the appended query parameter for the api key
    private static final String API_KEY_VALUE = "0d4d26dde08feee69c914af1a77fe3c4";

    // Use top rated to call the top rated sort
    private static final String TOP_RATED = "top_rated";

    // Use popular to call the popular sort.
    private static final String POPULAR = "popular";

    // Use videos to call the video results url.
    private static final String VIDEOS = "videos";

    // Use reviews to call the reviews results url.
    private static final String REVIEWS = "reviews";

    // add to the end of top rated or popular
    private static final String QUERY_PARAMETER = "?";

    // a forward slash to construct url paths
    private static final String FORWARD_SLASH = "/";

    /**
     * A method that returns a movie url with the popular movies tagged
     * @return a url with an api key appended
     */
    public static URL buildPopularMovieUrl() {

        String builtUrl = MOVIE_BASE_URL
                + POPULAR
                + QUERY_PARAMETER
                + API_KEY
                + API_KEY_VALUE;

        URL popularMovieUrL = null;
        try {
            popularMovieUrL = new URL(builtUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (popularMovieUrL != null) {
            Log.v("MOVIES", popularMovieUrL.toString());
        }
        return popularMovieUrL;
    }

    /**
     * A method that returns a url with the top rated movies tagged
     * @return The top rated movies url
     */
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

    public static URL buildVideoDatasetUrl(String movieId) {

        String builtUrl = MOVIE_BASE_URL
                + movieId
                + FORWARD_SLASH
                + VIDEOS
                + QUERY_PARAMETER
                + API_KEY
                + API_KEY_VALUE;

        URL videoDatasetUrl = null;
        try {
            videoDatasetUrl = new URL(builtUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (videoDatasetUrl != null) {
            Log.v("VIDEO SET", videoDatasetUrl.toString());
        }
        return  videoDatasetUrl;
    }

    public static URL buildReviewDatasetUrl(String movieId) {

        String builtUrl = MOVIE_BASE_URL
                + movieId
                + FORWARD_SLASH
                + REVIEWS
                + QUERY_PARAMETER
                + API_KEY
                + API_KEY_VALUE;

        URL reviewDatasetUrl = null;
        try {
            reviewDatasetUrl = new URL(builtUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (reviewDatasetUrl != null) {
            Log.v("REVIEW SET", reviewDatasetUrl.toString());
        }

        return reviewDatasetUrl;
    }

    /**
     * A helper method
     * @param url Takes a url built to specifications
     * @return 
     * @throws IOException Check for problems
     */
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
