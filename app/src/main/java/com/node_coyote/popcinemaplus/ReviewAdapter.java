package com.node_coyote.popcinemaplus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.node_coyote.popcinemaplus.data.Review;

import java.util.ArrayList;


/**
 * Created by node_coyote on 5/15/17.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(@NonNull Context context, ArrayList<Review> reviews) {
        super(context, 0, reviews);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View listView, @NonNull ViewGroup parent) {

        if (listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review, parent, false);
        }

        final Review currentReview = getItem(position);

        String currentAuthor = currentReview.getAuthor();
        TextView authorTextView = (TextView) listView.findViewById(R.id.author);
        if (currentAuthor != null) {
            authorTextView.setText(currentAuthor);
        }

        String currentContent = currentReview.getContent();
        Log.v("AUTHORCONTENT", currentContent);
        TextView contentTextView = (TextView) listView.findViewById(R.id.content);

        if (currentContent != null) {
            contentTextView.setText(currentContent);
        }

        return listView;
    }
}