package com.node_coyote.popcinemaplus.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.node_coyote.popcinemaplus.data.MovieContract.MovieEntry;

/**
 * Created by node_coyote on 5/16/17.
 */

public class MovieProvider extends ContentProvider {

    // A tag for log messages.
    public static final String LOG_TAG = MovieEntry.class.getSimpleName();

    // uri watcher code for the content uri for the movie database.
    private static final int MOVIE = 42;

    // uri watcher code for the content uri for a single movie in the movies database.
    private static final int MOVIE_ID = 9;

    // UriMatcher object to match a content uri to a code.
    private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // The items to match with the uri matcher. Is it a movie, or all movies?
    static {
        sMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, MOVIE);
        sMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", MOVIE_ID);
    }


    // A database helper object.
    private MovieDatabaseHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new MovieDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // We need a readable database to look at.
        SQLiteDatabase database = mHelper.getReadableDatabase();

        // A cursor movie gift box.
        Cursor cursor;

        // Match uri to code.
        int match = sMatcher.match(uri);
        switch (match) {
            case MOVIE:
                // look at the whole movie database.
                cursor = database.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MOVIE_ID:
                // query a row by id.
                // Add an additional parameter for an individual item in the database.
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return MovieEntry.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri" + uri + "with match" + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new RuntimeException("Use bulkInsert for PopCinema.");
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        // Get a writable database to fill with movie data goodies.
        final SQLiteDatabase database = mHelper.getWritableDatabase();

        switch (sMatcher.match(uri)) {
            case MOVIE:
                database.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = database.insert(MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        //Get the writable database.
        SQLiteDatabase database = mHelper.getWritableDatabase();

        // Track number of deleted rows.
        int deletedRows;

        final int match = sMatcher.match(uri);
        switch (match) {
            case MOVIE:
                deletedRows = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                deletedRows = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion not supported for " + uri);
        }

        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sMatcher.match(uri);
        // Let's check if we're updating the whole database or just a row.
        switch (match) {
            case MOVIE:
                return updateItem(uri, values, selection, selectionArgs);
            case MOVIE_ID:
                // Let's get the id from the uri so we know which row to update.
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Let's use this method mainly to update Favorites values from 0 to 1 and vice versa.
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    public int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs){

        //TODO Update Favorites.

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mHelper.getWritableDatabase();

        int updatedRows = database.update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);

        if (updatedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRows;

    }
}
