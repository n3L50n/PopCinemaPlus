package com.node_coyote.popcinema.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by node_coyote on 5/10/17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.node_coyote.popcinema.data";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        /**
         * The name of the database for movies
         */
        public static final String TABLE_NAME = "movies";

        /**
         * Type: INTEGER
         * Unique identifier of a single movie (row) selection
         */
        public static final String COLUMN_MOVIE_ID = "movie_id";

        /**
         * Type: TEXT
         * The title of a single movie selection
         */
        public static final String COLUMN_TITLE = "title";

        /**
         * Type: TEXT
         * The summary of a movie
         */
        public static final String COLUMN_SUMMARY = "summary";

        /**
         * Type: TEXT
         * The path to the poster image
         */
        public static final String COLUMN_POSTER = "poster";


        /**
         * Type: REAL
         * A float average of user votes on a movie, user ratings
         */
        public static final String COLUMN_RATING = "rating";

        /**
         * Type: TEXT
         * The release date is a string
         */
        public static final String COLUMN_RELEASE_DATE = "release";

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
    }
}
