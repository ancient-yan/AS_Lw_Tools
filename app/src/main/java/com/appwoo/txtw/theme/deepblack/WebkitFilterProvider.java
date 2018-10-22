package com.appwoo.txtw.theme.deepblack;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.appwoo.txtw.theme.deepblack.utils.WebkitFilterCursor;

public class WebkitFilterProvider extends ContentProvider {
    private final static String TAG = "my_log";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        if (selectionArgs == null || selectionArgs.length == 0)
        {
            return null;
        }

        final String packageName = selectionArgs[0];
        final String url = selectionArgs[1];

        Log.e(TAG, "WebkitFilterProvider.query.packageName : " + packageName);
        Log.e(TAG, "WebkitFilterProvider.query.url : " + url);

        Cursor cursor = new WebkitFilterCursor(packageName, url, 1);

        return cursor;
    }
}
