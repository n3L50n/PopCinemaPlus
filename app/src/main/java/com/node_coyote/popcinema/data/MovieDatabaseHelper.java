package com.node_coyote.popcinema.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.node_coyote.popcinema.data.MovieContract.MovieEntry;

/**
 * Created by node_coyote on 5/11/17.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    /**
     * This is the name of our database which will be saved locally on the user's device
     */
    public static final String DATABASE_NAME = "movies.db";

    /**
     * Increment this each time the database schema is changed.
     */
    private static final int DATABASE_VERSION = 1;

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is a required method override that can help us create a sql command to create
     * our movies database
     * @param database The sqlite database
     */
    @Override
    public void onCreate(SQLiteDatabase database) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " ("
                + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntry.COLUMN_MOVIE_ID + " INTEGER"
                + MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " ;


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
        database.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(database);
    }
}
