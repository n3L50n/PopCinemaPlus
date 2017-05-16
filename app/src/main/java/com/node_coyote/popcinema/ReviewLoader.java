package com.node_coyote.popcinema;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.node_coyote.popcinema.utility.JsonUtility;
import com.node_coyote.popcinema.utility.NetworkUtility;
import com.node_coyote.popcinema.utility.Review;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by node_coyote on 5/15/17.
 */

public class ReviewLoader { //extends AsyncTaskLoader<List<Review>> {

//    private URL mUrl;
//
//    public ReviewLoader(Context context, URL url) {
//        //super(context);
//        mUrl = url;
//    }
//
//    @Override
//    public List<Review> loadInBackground() {
//
//        if (mUrl == null) {
//            return null;
//        }
//
//        List<Review> reviews = null;
//        String f = null;
//        try {
//             f = NetworkUtility.getResponseFromHttp(mUrl);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////
////        try {
////             //reviews = JsonUtility.getReviewItemsFromJson(this, f);
////        } catch (JSONException e) {
////            Log.v("ReviewLoader", mUrl.toString());
////        }
//
//        return reviews;
//    }
//
//    @Override
//    //protected void onStartLoading() {
//        forceLoad();
//    }
}
