package com.jeffpalm.android.util.urls;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

final class DatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "DatabaseHelper";
	private static final int VERSION = 12;
	public static final String DATABASE_NAME = "db_urls";

	public static final String TABLE_URLS = "table_urls";

	public static final String COLUMN_URL = "url";
	public static final String COLUMN_BODY = "body";
	public static final String COLUMN_DATETIME = "datetime";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		Log.i(TAG, "DatabaseHelper created.");
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "
				+ TABLE_URLS + " ( " 
				+ COLUMN_URL + " TEXT ,"
				+ COLUMN_BODY + " TEXT ,"
				+ COLUMN_DATETIME + " int "
				+ ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("Drop table if exists " + TABLE_URLS);

		onCreate(db);
	}

}
