package com.node_coyote.popcinema.utility;

/**
 * Created by node_coyote on 5/1/17.
 */

import android.net.Uri;

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

    private static final String MOVIE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    private static final String API_KEY = "api_key=";

    private static final String API_KEY_VALUE = "REPLACE WITH API KEY FOR TESTING SO AS NOT TO COMMIT TO GITHUB";

    public static URL buildMovieUrl(String movieQuery) {

        Uri movieUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                //TODO build out dynamic way to query
                //.appendQueryParameter()
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();
        URL movieUrL = null;
        try {
            movieUrL = new URL(movieUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return movieUrL;
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
