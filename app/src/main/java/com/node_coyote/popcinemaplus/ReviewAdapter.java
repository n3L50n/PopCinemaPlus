package com.node_coyote.popcinemaplus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by node_coyote on 5/15/17.
 */

public class ReviewAdapter {

//    public ReviewAdapter(@NonNull Context context, ArrayList<> reviews) {
//        super(context, 0, reviews);
//    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View listItem, @NonNull ViewGroup parent) {
//
//        if (listItem == null) {
//            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review, parent, false);
//        }
//
//        final Review currentReview = getItem(position);
//
//        String currentAuthor = currentReview.getAuthor();
//        TextView authorTextView = (TextView) listItem.findViewById(R.id.author);
//
//        if (currentAuthor != null) {
//            authorTextView.setText(currentAuthor);
//        }
//
//        String currentContent = currentReview.getContent();
//        TextView contentTextView = (TextView) listItem.findViewById(R.id.content);
//        if (currentContent != null) {
//            contentTextView.setText(currentContent);
//        }
//
//        return listItem;
//    }
}
