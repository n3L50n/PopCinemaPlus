package com.node_coyote.popcinema.utility;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by node_coyote on 5/2/17.
 */

public final class MovieJsonUtility {

    public static String[] getMovieStringsFromJson(Context context, String movieJsonString) throws JSONException {

        String[] parsedMovieData = null;

        JSONObject root = new JSONObject(movieJsonString);

        return parsedMovieData;
    }

}
