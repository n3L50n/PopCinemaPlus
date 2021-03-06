package com.node_coyote.popcinemaplus.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.node_coyote.popcinemaplus.data.MovieContract.MovieEntry;

/**
 * Created by node_coyote on 5/11/17.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    /**
     * This is the name of our database which will be saved locally on the user's device
     */
    private static final String DATABASE_NAME = "movies.db";

    /**
     * Increment this each time the database schema is changed.
     */
    private static final int DATABASE_VERSION = 1;

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * A string to create our movies database.
     */
    private static final String SQL_CREATE_MOVIE_TABLE =

            "CREATE TABLE " + MovieEntry.TABLE_NAME + " ("
                    + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MovieEntry.COLUMN_POSTER + " TEXT, "
                    + MovieEntry.COLUMN_SUMMARY + " TEXT, "
                    + MovieEntry.COLUMN_RELEASE_DATE + " TEXT, "
                    + MovieEntry.COLUMN_MOVIE_ID + " INTEGER, "
                    + MovieEntry.COLUMN_TITLE + " TEXT, "
                    + MovieEntry.COLUMN_POPULARITY + " REAL, "
                    + MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, "
                    + MovieEntry.COLUMN_TRAILER_SET + " TEXT, "
                    + MovieEntry.COLUMN_TRAILER + " TEXT, "
                    + MovieEntry.COLUMN_FAVORITE + " INTEGER NOT NULL DEFAULT 0, "
                    + MovieEntry.COLUMN_REVIEW_SET + " TEXT);";

    /**
     * This is a required method override that can help us create a sql command to create
     * our movies database
     * @param database The sqlite database
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
            database.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    /**
     * Use this method to eliminate an old database and swap it for a new schema.
     * @param database the old database
     * @param oldVersion Old DATABASE_VERSION
     * @param newVersion New DATABASE_VERSION (make sure to upgrade the constant.)
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(" DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(database);
    }

    /**
     * A method to help sort the movie grid posters according to popularity, rating, or favorites values.
     * @param columns The specific column to be sorted.
     * @param sortOrder Whether to sort ascending or descending.
     * @return
     */
    public Cursor getSortOrder(String[] columns, String sortOrder) {

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(MovieEntry.TABLE_NAME);

        Cursor cursor = builder.query(getReadableDatabase(), columns,
                null, null, null, null, sortOrder);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()){
            cursor.close();
            return null;
        }
        return cursor;
    }
}
