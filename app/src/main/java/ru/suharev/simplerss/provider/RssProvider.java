package ru.suharev.simplerss.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

public class RssProvider extends ContentProvider {
    private static final String DATABASE_NAME = "RSS_database";

    private static final String SORT_ORDER_ID = Columns._ID + " ASC";

    private static final String SCHEME = "content://";
    private static final String SLASH = "/";
    private static final String AUTHORITIES = "ru.suharev.simplerss";

    private static UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sMatcher.addURI(AUTHORITIES, Tables.RSS, UriCodes.URI_RSS);
    }

    private DatabaseHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        if (sortOrder == null) sortOrder = SORT_ORDER_ID;
        switch (sMatcher.match(uri)) {
            case UriCodes.URI_RSS:
                return db.query(Tables.RSS, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                return null;
        }

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        switch (sMatcher.match(uri)){
            case UriCodes.URI_RSS:
                Long id  = db.insert(Tables.RSS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        switch (sMatcher.match(uri)){
            case UriCodes.URI_RSS:
                int count =  db.delete(Tables.RSS, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                return count;
            default:
                return 0;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        switch (sMatcher.match(uri)){
            case UriCodes.URI_RSS:
                int count = db.update(Tables.RSS, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                return count;
            default:
                return 0;
        }
    }


    public static class UriCodes{

        public static final int URI_RSS = 1;
    }

    public static class Uris{

        public static final Uri URI_RSS = Uri.parse(SCHEME + AUTHORITIES + SLASH + Tables.RSS);

    }

    public static class Tables{

        public static final String RSS = "RSS_table";

    }

    public static class Columns implements BaseColumns {
        
        public static final String RSS_URI = "RSS_uri";
        public static final String NAME = "title";

    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        static final int VERSION = 1;

        static final String CREATE_TABLE_RSS = "CREATE TABLE " + Tables.RSS + " (" +
                Columns._ID + " INTEGER PRIMARY KEY, " +
                Columns.RSS_URI + " TEXT, " +
                Columns.NAME + " TEXT);";

        static final String DROP_TABLE_RSS = "DROP TABLE IF EXISTS " + Tables.RSS;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_RSS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE_RSS);
            onCreate(db);
        }
    }
}
