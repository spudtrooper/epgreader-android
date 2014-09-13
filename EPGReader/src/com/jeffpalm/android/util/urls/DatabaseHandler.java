package com.jeffpalm.android.util.urls;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

final class DatabaseHandler {

  public interface CachedUrlResult {
    /**
     * @return the content of the url
     */
    String getBody();

    /**
     * @return the URL with prototcol and host
     */
    String getUrl();

    /**
     * @return the time in milliseconds when this entry watch cached or -1 if it was not cached. The
     *         time is determined by {@code DatabaseHandler#now()}.
     */
    long getTimeMillis();
  }

  private static final class UrlResultImpl implements CachedUrlResult {

    private final String url;
    private final String body;
    private final long timeMillis;

    public UrlResultImpl(String url, String body, long timeMillis) {
      this.url = url;
      this.body = body;
      this.timeMillis = timeMillis;
    }

    @Override
    public String getBody() {
      return body;
    }

    @Override
    public String getUrl() {
      return url;
    }

    @Override
    public long getTimeMillis() {
      return timeMillis;
    }

  }

  /**
   * Returns the formatted string for the result.
   * 
   * @param urlResult
   * @return the formatted string for the result
   */
  public static String toString(CachedUrlResult urlResult) {
    StringBuilder sb = new StringBuilder();
    sb.append("url=").append(urlResult.getUrl());
    sb.append(", timeMillis=").append(urlResult.getTimeMillis());
    return sb.toString();
  }

  private DatabaseHelper dbHelper;
  private SQLiteDatabase database;

  public DatabaseHandler(Context context) {
    dbHelper = new DatabaseHelper(context);

  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public void clearTable(String tableName) {
    database.delete(tableName, null, null);
  }

  public void insertUrl(String url, String body) {
    ContentValues cv = new ContentValues();

    cv.put(DatabaseHelper.COLUMN_URL, url);
    cv.put(DatabaseHelper.COLUMN_BODY, body);
    cv.put(DatabaseHelper.COLUMN_DATETIME, now());

    database.insert(DatabaseHelper.TABLE_URLS, DatabaseHelper.COLUMN_URL, cv);
  }

  /** @return the time in millis used for timestamps. */
  public static long now() {
    return SystemClock.uptimeMillis();
  }

  public List<CachedUrlResult> getAll() {
    List<CachedUrlResult> urlResults = new ArrayList<CachedUrlResult>();
    Cursor cursor = database.rawQuery("select " + DatabaseHelper.COLUMN_URL + ", "
        + DatabaseHelper.COLUMN_BODY + ", " + DatabaseHelper.COLUMN_DATETIME + " FROM "
        + DatabaseHelper.TABLE_URLS + " where 1", new String[] {});

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      String body = cursor.getString(0);
      String url = cursor.getString(1);
      long dateTime = cursor.getInt(2);
      urlResults.add(new UrlResultImpl(url, body, dateTime));
    }
    cursor.close();
    return urlResults;
  }

  /**
   * @param url the cached URL
   * @return the cached result for the URL or null
   */
  public CachedUrlResult getUrlResult(String url) {
    Cursor cursor = database.rawQuery("select " + DatabaseHelper.COLUMN_BODY + ", "
        + DatabaseHelper.COLUMN_DATETIME + " FROM " + DatabaseHelper.TABLE_URLS + " where "
        + DatabaseHelper.COLUMN_URL + " = ?", new String[] { url });

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      String body = cursor.getString(0);
      long dateTime = cursor.getInt(1);
      return new UrlResultImpl(url, body, dateTime);
    }
    cursor.close();

    // Didn't find the url
    return null;
  }

  /**
   * @return the number of cached entries.
   */
  public int getNumEntries() {
    Cursor cursor = database.rawQuery("select count(*) where 1", new String[] {});
    cursor.moveToFirst();
    int numEntries = cursor.getInt(0);
    cursor.close();
    return numEntries;
  }

}
