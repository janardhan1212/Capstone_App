package com.janardhan.blood2life.Helpers;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by janardhanyerranagu on 05/11/16.
 */

public class MyProvider extends ContentProvider {
    // Indicates that the incoming query is for a USER URL
    public static final int IMAGE_URL_QUERY = 1;

    // Indicates that the incoming query is for a URL modification date
    public static final int URL_DATE_QUERY = 2;

    // Indicates an invalid content URI
    public static final int INVALID_URI = -1;
    // Identifies log statements issued by this component
    public static final String LOG_TAG = "DataProvider";
    // Constants for building SQLite tables during initialization
    private static final String TEXT_TYPE = "TEXT";
    private static final String PRIMARY_KEY_TYPE = "INTEGER PRIMARY KEY";
    private static final String INTEGER_TYPE = "INTEGER";
    // Defines an SQLite statement that builds the Picasa USER URL table
    private static final String CREATE_USER_URL_TABLE_SQL = "CREATE TABLE " + DataProviderContract.USERTABLENAME + "("
            + DataProviderContract.KEY_ID + " INTEGER PRIMARY KEY," + DataProviderContract.KEY_UID + " TEXT," + DataProviderContract.KEY_NAME + " TEXT,"
            + DataProviderContract.KEY_EMAIL + " TEXT UNIQUE," + DataProviderContract.KEY_PHNO + " INTEGER,"
            + DataProviderContract.KEY_GROUP + " TEXT ,"
            + DataProviderContract.KEY_CITY + " TEXT ,"
            + DataProviderContract.KEY_STATE + " TEXT ,"
            + DataProviderContract.KEY_pic_url + " TEXT ,"
            + DataProviderContract.KEY_TOKEN + " TEXT " + ")";
    // Defines a helper object that matches content URIs to table-specific parameters
    private static final UriMatcher sUriMatcher;

    /*
     * Initializes meta-data used by the content provider:
     * - UriMatcher that maps content URIs to codes
     * - MimeType array that returns the custom MIME type of a table
     */
    static {

        // Creates an object that associates content URIs with numeric codes
        sUriMatcher = new UriMatcher(0);


        // Adds a URI "match" entry that maps USER URL content URIs to a numeric code
        sUriMatcher.addURI(
                DataProviderContract.AUTHORITY,
                DataProviderContract.USERTABLENAME,
                IMAGE_URL_QUERY);

        // Adds a URI "match" entry that maps modification date content URIs to a numeric code
        sUriMatcher.addURI(
                DataProviderContract.AUTHORITY,
                DataProviderContract.DATE_TABLE_NAME,
                URL_DATE_QUERY);

        // Specifies a custom MIME type for the USER URL table


    }

    // Defines an helper object for the backing database
    private SQLiteOpenHelper mHelper;

    // Closes the SQLite database helper class, to avoid memory leaks
    public void close() {
        mHelper.close();
    }

    /**
     * Initializes the content provider. Notice that this method simply creates a
     * the SQLiteOpenHelper instance and returns. You should do most of the initialization of a
     * content provider in its static initialization block or in SQLiteDatabase.onCreate().
     */
    @Override
    public boolean onCreate() {

        // Creates a new database helper object
        mHelper = new DataProviderHelper(getContext());

        return true;
    }

    /**
     * Returns the result of querying the chosen table.
     *
     * @param uri           The content URI of the table
     * @param projection    The names of the columns to return in the cursor
     * @param selection     The selection clause for the query
     * @param selectionArgs An array of Strings containing search criteria
     * @param sortOrder     A clause defining the order in which the retrieved rows should be sorted
     * @return The query results, as a {@link android.database.Cursor} of rows and columns
     * @see android.content.ContentProvider#query(Uri, String[], String, String[], String)
     */
    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        // Decodes the content URI and maps it to a code
        switch (sUriMatcher.match(uri)) {

            // If the query is for a USER URL
            case IMAGE_URL_QUERY:
                // Does the query against a read-only version of the database
                Cursor returnCursor = db.query(
                        DataProviderContract.USERTABLENAME,
                        projection,
                        null, null, null, null, null);

                // Sets the ContentResolver to watch this content URI for data changes
                returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return returnCursor;

            // If the query is for a modification date URL
            case URL_DATE_QUERY:
                returnCursor = db.query(
                        DataProviderContract.DATE_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                // No notification Uri is set, because the data doesn't have to be watched.
                return returnCursor;

            case INVALID_URI:

                throw new IllegalArgumentException("Query -- Invalid URI:" + uri);
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // Decode the URI to choose which action to take
        switch (sUriMatcher.match(uri)) {

            // For the modification date table
            case URL_DATE_QUERY:

                // Creates a writeable database or gets one from cache
                SQLiteDatabase localSQLiteDatabase = mHelper.getWritableDatabase();

                // Inserts the row into the table and returns the new row's _id value
                long id = localSQLiteDatabase.insert(
                        DataProviderContract.DATE_TABLE_NAME,
                        DataProviderContract.DATA_DATE_COLUMN,
                        values
                );

                // If the insert succeeded, notify a change and return the new row's content URI.
                if (-1 != id) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return Uri.withAppendedPath(uri, Long.toString(id));
                } else {

                    throw new SQLiteException("Insert error:" + uri);
                }
            case IMAGE_URL_QUERY:

                throw new IllegalArgumentException("Insert: Invalid URI" + uri);
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        throw new UnsupportedOperationException("Delete -- unsupported operation " + uri);
    }

    /**
     * Updates one or more rows in a table.
     * @see android.content.ContentProvider#update(Uri, ContentValues, String, String[])
     * @param uri The content URI for the table
     * @param values The values to use to update the row or rows. You only need to specify column
     * names for the columns you want to change. To clear the contents of a column, specify the
     * column name and NULL for its value.
     * @param selection An SQL WHERE clause (without the WHERE keyword) specifying the rows to
     * update. Use "?" to mark places that should be substituted by values in selectionArgs.
     * @param selectionArgs An array of values that are mapped in order to each "?" in selection.
     * If no "?" are used, set this to NULL.
     *
     * @return int The number of rows updated.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        // Decodes the content URI and choose which insert to use
        switch (sUriMatcher.match(uri)) {

            // A USER URL content URI
            case URL_DATE_QUERY:

                // Creats a new writeable database or retrieves a cached one
                SQLiteDatabase localSQLiteDatabase = mHelper.getWritableDatabase();

                // Updates the table
                int rows = localSQLiteDatabase.update(
                        DataProviderContract.DATE_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                // If the update succeeded, notify a change and return the number of updated rows.
                if (0 != rows) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return rows;
                } else {

                    throw new SQLiteException("Update error:" + uri);
                }

            case IMAGE_URL_QUERY:

                throw new IllegalArgumentException("Update: Invalid URI: " + uri);
        }

        return -1;
    }

    /**
     * Defines a helper class that opens the SQLite database for this provider when a request is
     * received. If the database doesn't yet exist, the helper creates it.
     */
    private class DataProviderHelper extends SQLiteOpenHelper {
        /**
         * Instantiates a new SQLite database using the supplied database name and version
         *
         * @param context The current context
         */
        DataProviderHelper(Context context) {
            super(context,
                    DataProviderContract.DATABASE_NAME,
                    null,
                    DataProviderContract.DATABASE_VERSION);
        }


        /**
         * Executes the queries to drop all of the tables from the database.
         *
         * @param db A handle to the provider's backing database.
         */
        private void dropTables(SQLiteDatabase db) {

            // If the table doesn't exist, don't throw an error
            db.execSQL("DROP TABLE IF EXISTS " + DataProviderContract.USERTABLENAME);
            db.execSQL("DROP TABLE IF EXISTS " + DataProviderContract.DATE_TABLE_NAME);
        }

        /**
         * Does setup of the database. The system automatically invokes this method when
         * SQLiteDatabase.getWriteableDatabase() or SQLiteDatabase.getReadableDatabase() are
         * invoked and no db instance is available.
         *
         * @param db the database instance in which to create the tables.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Creates the tables in the backing database for this provider
            db.execSQL(CREATE_USER_URL_TABLE_SQL);

        }

        /**
         * Handles upgrading the database from a previous version. Drops the old tables and creates
         * new ones.
         *
         * @param db The database to upgrade
         * @param version1 The old database version
         * @param version2 The new database version
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int version1, int version2) {
            Log.w(DataProviderHelper.class.getName(),
                    "Upgrading database from version " + version1 + " to "
                            + version2 + ", which will destroy all the existing data");

            // Drops all the existing tables in the database
            dropTables(db);

            // Invokes the onCreate callback to build new tables
            onCreate(db);
        }

        /**
         * Handles downgrading the database from a new to a previous version. Drops the old tables
         * and creates new ones.
         *
         * @param db       The database object to downgrade
         * @param version1 The old database version
         * @param version2 The new database version
         */
        @Override
        public void onDowngrade(SQLiteDatabase db, int version1, int version2) {
            Log.w(DataProviderHelper.class.getName(),
                    "Downgrading database from version " + version1 + " to "
                            + version2 + ", which will destroy all the existing data");

            // Drops all the existing tables in the database
            dropTables(db);

            // Invokes the onCreate callback to build new tables
            onCreate(db);

        }

        public void addUser(String uid, String name, String email, String phno, String bgroup, String city, String state, String refreshedToken, String pro_url) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DataProviderContract.KEY_UID, uid);
            values.put(DataProviderContract.KEY_NAME, name);
            values.put(DataProviderContract.KEY_EMAIL, email); // Email
            values.put(DataProviderContract.KEY_PHNO, phno); // Name
            values.put(DataProviderContract.KEY_GROUP, bgroup); // Email
            values.put(DataProviderContract.KEY_CITY, city);
            values.put(DataProviderContract.KEY_STATE, state); // Email
            values.put(DataProviderContract.KEY_pic_url, pro_url);
            values.put(DataProviderContract.KEY_TOKEN, refreshedToken);
            // Created At

            // Inserting Row
            long id = db.insert(DataProviderContract.USERTABLENAME, null, values);
            db.close(); // Closing database connection

            Log.d(TAG, "New user inserted into sqlite: " + id);
        }

        /**
         * Getting user data from database
         */
        public HashMap<String, String> getUserDetails() {
            HashMap<String, String> user = new HashMap<String, String>();
            String selectQuery = "SELECT  * FROM " + DataProviderContract.USERTABLENAME;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // Move to first row
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                user.put(DataProviderContract.KEY_UID, cursor.getString(1));
                user.put("name", cursor.getString(2));

                user.put("email", cursor.getString(3));
                user.put("phno", cursor.getString(4));
                user.put("bgroup", cursor.getString(5));
                user.put("city", cursor.getString(6));
                user.put("state", cursor.getString(7));
                user.put("profile_pic_url", cursor.getString(8));
                user.put("refreshedToken", cursor.getString(9));
            }
            cursor.close();
            db.close();
            // return user
            Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

            return user;
        }
    }
}
