package com.appwoo.txtw.theme.deepblack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/8/3.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;

    public DBOpenHelper(Context context, String DB_NAME)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public SQLiteDatabase open() throws SQLiteException {
        try {
            return getWritableDatabase();
        } catch (SQLiteException e) {
            return getReadableDatabase();
        }
    }
}
