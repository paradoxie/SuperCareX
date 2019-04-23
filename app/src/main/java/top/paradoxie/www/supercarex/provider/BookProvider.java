package top.paradoxie.www.supercarex.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class BookProvider extends ContentProvider {

    private Context context;

    private SQLiteDatabase sqLiteDatabase;

    public static final String AUTHORITY = "top.paradoxie.www.supercarex.provider.BookProvider";


    public static final int USER_URI_CODE = 1;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, DbOpenHelper.USER_TABLE_NAME, USER_URI_CODE);
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (uriMatcher.match(uri)) {
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
        }
        return tableName;
    }

    public BookProvider() {

    }

    @Override
    public boolean onCreate() {
        context = getContext();
        initProviderData();
        return false;
    }

    //初始化原始数据
    private void initProviderData() {
        sqLiteDatabase = new DbOpenHelper(context).getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        sqLiteDatabase.insert(tableName, null, values);
        context.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        return sqLiteDatabase.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        int row = sqLiteDatabase.update(tableName, values, selection, selectionArgs);
        if (row > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        int count = sqLiteDatabase.delete(tableName, selection, selectionArgs);
        if (count > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

}
