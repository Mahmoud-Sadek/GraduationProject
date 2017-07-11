package com.sadek.apps.freelance7rfeen.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Mahmoud Sadek on 3/11/2017.
 */
public class FreelanceProvider extends ContentProvider {

    FreelanceDbHelper freelanceDbHelper;
    public final UriMatcher uriMatcher = buildUriMatcher();

    static final int MOVIE_DETAILS = 100;
    static final int MOVIEDETAILS_WITH_ID = 101;

    static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FreelanceContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, FreelanceContract.PATH_FREELANCE, MOVIE_DETAILS);
        uriMatcher.addURI(authority, FreelanceContract.PATH_FREELANCE + "/*", MOVIEDETAILS_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        freelanceDbHelper = new FreelanceDbHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIE_DETAILS:
                return FreelanceContract.FreelanceEntry.CONTENT_TYPE;
            case MOVIEDETAILS_WITH_ID:
                return FreelanceContract.FreelanceEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = freelanceDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);

        switch (match) {
            case MOVIE_DETAILS:
                cursor = db.query(
                        FreelanceDbHelper.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIEDETAILS_WITH_ID:
                long _id = ContentUris.parseId(uri);
                cursor = db.query(
                        FreelanceDbHelper.TABLE_NAME,
                        projection,
                        FreelanceContract.FreelanceEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = freelanceDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnedUri = null;

        switch (match) {
            case MOVIE_DETAILS:
                long _id = db.insert(FreelanceDbHelper.TABLE_NAME, null, values);
                if (_id != -1) {
                    returnedUri = FreelanceContract.FreelanceEntry.buildDetailsUri(_id);
                } else {
                    Log.e("tag", "insert: returnedUri");
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnedUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = freelanceDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowdelete;
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIE_DETAILS:
                rowdelete = db.delete(FreelanceDbHelper.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowdelete != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowdelete;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
