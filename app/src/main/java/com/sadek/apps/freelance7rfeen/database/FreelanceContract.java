package com.sadek.apps.freelance7rfeen.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mahmoud Sadek on 3/11/2017.
 */
public class FreelanceContract {

    public static final String CONTENT_AUTHORITY = "com.sadek.apps.freelance7rfeen";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FREELANCE = "freelance";

    public static final class FreelanceEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FREELANCE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FREELANCE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FREELANCE;


        public static Uri buildDetailsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}