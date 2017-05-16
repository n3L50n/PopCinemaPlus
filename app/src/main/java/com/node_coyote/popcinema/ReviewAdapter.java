package com.node_coyote.popcinema;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.node_coyote.popcinema.utility.Review;

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
    public View getView(int position, @Nullable View listItem, @NonNull ViewGroup parent) {

        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review, parent, false);
        }

        final Review currentReview = getItem(position);

        TextView authorTextView = (TextView) listItem.findViewById(R.id.author);
        String currentAuthor = currentReview.getAuthor();
        if (currentAuthor != null) {
            authorTextView.setText(currentAuthor);
        }

        TextView contentTextView = (TextView) listItem.findViewById(R.id.content);
        String currentContent = currentReview.getContent();
        if (currentContent != null) {
            contentTextView.setText(currentContent);
        }


        return listItem;
    }
}
