package com.node_coyote.popcinemaplus;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.node_coyote.popcinemaplus.data.MovieContract.MovieEntry;

import java.util.ArrayList;

/**
 * Created by node_coyote on 5/15/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{

    private Cursor mCursor;

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
        }
    }

//    public ReviewAdapter(@NonNull Context context, Cursor cursor) {
//        super(context, 0, cursor);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View listItem, @NonNull ViewGroup parent) {
//
//        if (listItem == null) {
//            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review, parent, false);
//        }
//
//
//
//        final Review currentReview = getItem(position);
//
//        String currentAuthor = currentReview.getAuthor();
//
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

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_review, parent);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        //int reviewColumnIndex = mCursor.getColumnIndex(MovieEntry.);

        //TextView authorTextView = (TextView) findViewById(R.id.author);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }
}
