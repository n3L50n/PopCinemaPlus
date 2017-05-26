package com.node_coyote.popcinemaplus.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by node_coyote on 5/10/17.
 */

public class MovieContract {

    // Empty constructor. Let's not instantiate the contract class.
    public MovieContract(){}

    public static final String CONTENT_AUTHORITY = "com.node_coyote.popcinemaplus.data";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        /**
         *
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +  PATH_MOVIE;

        /**
         *
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +  PATH_MOVIE;

        /**
         * The name of the database for movies
         */
        public static final String TABLE_NAME = "movies";

        /**
         * Type: INTEGER
         * Unique identifier for a movie (row) in the database
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Type: TEXT
         * The path to the poster image
         */
        public static final String COLUMN_POSTER = "poster_path";

        /**
         * Type: TEXT
         * The summary of a movie
         */
        public static final String COLUMN_SUMMARY = "overview";

        /**
         * Type: TEXT
         * The release date is a string
         */
        public static final String COLUMN_RELEASE_DATE = "release_date";

        /**
         * Type: INTEGER
         * Unique identifier of a single movie in the json
         */
        public static final String COLUMN_MOVIE_ID = "id";

        /**
         * Type: TEXT
         * The title of a single movie selection
         */
        public static final String COLUMN_TITLE = "original_title";

        /**
         * Type: REAL
         * The level of popularity of a movie selection, described using a float.
         */
        public static final String COLUMN_POPULARITY = "popularity";

        /**
         * Type: REAL
         * A float average of user votes on a movie, user ratings
         */
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        /**
         * Type: TEXT
         * The path to a set of trailers
         */
        public static final String COLUMN_TRAILER_SET = "trailer_set";

        /**
         * Type: TEXT
         * The path to an individual trailer within the set of trailers
         */
        public static final String COLUMN_TRAILER = "trailer";

        /**
         * Type: INTEGER
         * Whether or not a user has added this movie as a favorite
         *  0 = false (DEFAULT)
         *  1 = true
         */
        public static final String COLUMN_FAVORITE = "favorite";

        /**
         * Type: TEXT
         * The path to a set of reviews.
         */
        public static final String COLUMN_REVIEW_SET = "review_set";
    }
}
