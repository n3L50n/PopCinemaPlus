package com.node_coyote.popcinemaplus;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.node_coyote.popcinemaplus.data.MovieContract.MovieEntry;


/**
 * Created by node_coyote on 5/15/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{

    private Cursor mCursor;
    public TextView mAuthorView;
    public TextView mContentView;

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mAuthorView = (TextView) itemView.findViewById(R.id.author);
            mContentView = (TextView) itemView.findViewById(R.id.content);

        }
    }

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
        int authorColumnIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_AUTHOR);
        int contentColumnIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_CONTENT);
        String author = mCursor.getString(authorColumnIndex);
        String content = mCursor.getString(contentColumnIndex);

        mAuthorView.setText(author);
        mContentView.setText(content);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }
}
