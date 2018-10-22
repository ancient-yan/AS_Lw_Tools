package com.appwoo.txtw.theme.deepblack.utils;

import android.database.AbstractCursor;
import android.text.TextUtils;

public class WebkitFilterCursor extends AbstractCursor {

    private int isBlack;

    private String url;
    private String packageName;

    private static final String COLUMN_URL = "url";
    private static final String COLUMN_IS_BLACK = "isBlack";
    private static final String COLUMN_PACKAGE_NAME = "packageName";

    public WebkitFilterCursor(String packageName, String url, int isBlack) {
        this.url = url;
        this.isBlack = isBlack;
        this.packageName = packageName;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{COLUMN_URL, COLUMN_PACKAGE_NAME, COLUMN_IS_BLACK};
    }

    @Override
    public String getString(int column) {
        String[] names = getColumnNames();
        String name = names[column];
        if (name.equals(COLUMN_URL)) {
            return String.valueOf(url);
        } else if (name.equals(COLUMN_PACKAGE_NAME)) {
            return String.valueOf(packageName);
        } else if (name.equals(COLUMN_IS_BLACK)) {
            return String.valueOf(isBlack);
        }
        return null;
    }

    @Override
    public short getShort(int column) {
        return 0;
    }

    @Override
    public int getInt(int column) {
        String[] names = getColumnNames();
        String name = names[column];
        if (name.equals(COLUMN_IS_BLACK)) {
            return isBlack;
        }
        return 0;
    }

    @Override
    public long getLong(int column) {
        String[] names = getColumnNames();
        String name = names[column];
        if (name.equals(COLUMN_IS_BLACK)) {
            return isBlack;
        }
        return 0;
    }

    @Override
    public float getFloat(int column) {
        String[] names = getColumnNames();
        String name = names[column];
        if (name.equals(COLUMN_IS_BLACK)) {
            return isBlack;
        }
        return 0;
    }

    @Override
    public double getDouble(int column) {
        String[] names = getColumnNames();
        String name = names[column];
        if (name.equals(COLUMN_IS_BLACK)) {
            return isBlack;
        }
        return 0;
    }

    @Override
    public boolean isNull(int column) {
        String[] names = getColumnNames();
        String name = names[column];
        if (name.equals(COLUMN_URL)) {
            return TextUtils.isEmpty(url);
        } else if (name.equals(COLUMN_PACKAGE_NAME)) {
            return TextUtils.isEmpty(packageName);
        } else if (name.equals(COLUMN_IS_BLACK)) {
            return false;
        }
        return true;
    }

}
