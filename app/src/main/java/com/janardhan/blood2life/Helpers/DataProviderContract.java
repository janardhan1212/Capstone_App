package com.janardhan.blood2life.Helpers;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by janardhanyerranagu on 07/11/16.
 */

public class DataProviderContract implements BaseColumns {

    // The URI scheme used for content URIs
    public static final String SCHEME = "content";
    // The provider's authority
    public static final String AUTHORITY = "com.janardhan.blood2life.Helpers";
    /**
     * The DataProvider content URI
     */
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);
    /**
     * The MIME type for a content URI that would return multiple rows
     * <P>Type: TEXT</P>
     */
    public static final String MIME_TYPE_ROWS =
            "vnd.android.cursor.dir/vnd.com.example.android.threadsample";
    /**
     * The MIME type for a content URI that would return a single row
     * <P>Type: TEXT</P>
     */
    public static final String MIME_TYPE_SINGLE_ROW =
            "vnd.android.cursor.item/vnd.com.example.android.threadsample";
    /**
     * Picture table primary key column name
     */
    public static final String ROW_ID = BaseColumns._ID;
    /**
     * Picture table name
     */
    public static final String USERTABLENAME = "user_table";
    /**
     * Picture table content URI
     */
    public static final Uri PICTUREURL_TABLE_CONTENTURI =
            Uri.withAppendedPath(CONTENT_URI, USERTABLENAME);
    /**
     * Picture table thumbnail URL column name
     */
    public static final String IMAGE_THUMBURL_COLUMN = "ThumbUrl";
    /**
     * Picture table thumbnail filename column name
     */
    public static final String KEY_ID = "id";
    public static final String KEY_UID = "uid";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHNO = "phno";
    public static final String KEY_CAT = "cat";
    public static final String KEY_GROUP = "bgroup";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_pic_url = "profile_pic_url";
    public static final String KEY_TOKEN = "refreshedToken";
    /**
     * Modification date table name
     */
    public static final String DATE_TABLE_NAME = "DateMetadatData";
    /**
     * Content URI for modification date table
     */
    public static final Uri DATE_TABLE_CONTENTURI =
            Uri.withAppendedPath(CONTENT_URI, DATE_TABLE_NAME);
    /**
     * Modification date table date column name
     */
    public static final String DATA_DATE_COLUMN = "DownloadDate";
    // The content provider database name
    public static final String DATABASE_NAME = "bloodtolife";
    // The starting version of the database
    public static final int DATABASE_VERSION = 1;

    public DataProviderContract() {
    }
}
