package com.node_coyote.popcinema.utility;

/**
 * Created by node_coyote on 5/15/17.
 */

public class Review {

    private String mAuthor;
    private String mContent;

    public Review (String author, String content) {
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
