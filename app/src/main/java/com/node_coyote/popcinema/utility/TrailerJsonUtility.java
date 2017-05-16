package com.node_coyote.popcinema.utility;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by node_coyote on 5/15/17.
 */

public class TrailerJsonUtility {

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
}
