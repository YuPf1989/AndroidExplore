package com.rain.androidexplore.content_provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Author:rain
 * Date:2018/7/31 15:52
 * Description:
 * 自定义contentProvider
 * note:
 * query,insert,delete,update是存在多线程并发的
 * openhelper内部同步了线程，但是多个openhelper不能保证线程同步
 */
public class BookProvider extends ContentProvider {
    private static final String TAG = "BookProvider";
    private static final String AUTHORITY = "com.rain.androidexplore.content_provider.BookProvider";
    private static final int BOOK_URI_CODE = 0;
    private static final int USER_URI_CODE = 1;

    private static final UriMatcher uriMather = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMather.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        uriMather.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private Context mContext;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Log.e(TAG, "onCreate: thread:" + Thread.currentThread().getName());
        mContext = getContext();
        initProviderData();
        return true;
    }

    private void initProviderData() {
        db = new DbOpenHelper(mContext).getWritableDatabase();
        db.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
        db.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
        db.execSQL("insert into book values(3,'Android')");
        db.execSQL("insert into book values(4,'java')");
        db.execSQL("insert into book values(5,'Html5')");
        db.execSQL("insert into user values(1,'john',20)");
        db.execSQL("insert into user values(2,'niky',21)");
        db.execSQL("insert into user values(3,'mike',22)");
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        int code = uriMather.match(uri);
        switch (code) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;

            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
        }
        return tableName;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e(TAG, "query: thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("unsupport uri:" + uri);
        }
        return db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("unsupport uri:" + uri);
        }
        long l = db.insert(table, null, values);
        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("unsupport uri:" + uri);
        }
        int count = db.delete(table, selection, selectionArgs);
        if (count > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("unsupport uri:" + uri);
        }
        int row = db.update(table, values, selection, selectionArgs);
        if (row > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return row;
    }
}
