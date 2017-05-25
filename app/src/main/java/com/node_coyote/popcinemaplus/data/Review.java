package com.node_coyote.popcinemaplus.data;

/**
 * Created by node_coyote on 5/24/17.
 */

public class Review {

    public String mAuthor;
    public String mContent;

    public Review(String author, String content) {
        mAuthor = author;
        mContent = content;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

}
