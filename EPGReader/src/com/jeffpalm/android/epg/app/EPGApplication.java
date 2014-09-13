package com.jeffpalm.android.epg.app;

import java.io.InputStream;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jeffpalm.android.util.urls.URLCache;

public class EPGApplication extends Application {

  private static final String TAG = "EGPApplication";

  @Override
  public void onCreate() {
    super.onCreate();
    new LoadMainPageTask(this).execute();
  }

  private static final class LoadMainPageTask extends AsyncTask<Void, Void, Void> {

    private final Context context;

    LoadMainPageTask(Context context) {
      this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
      // Flush the main URL from the cache.
      try {
        Log.d(TAG, "Forcing a load of the main page.");
        String url = EPGReaderSharedPrefs.getEpgUrl(context);
        URLCache urlCache = EPGReaderUtil.getURLCache(context);
        if (BuildConfig.DEBUG) {
          Log.d(TAG, "URL cache has " + urlCache.getNumEntries() + " entries");
        }
        InputStream is = urlCache.openStream(url, true /* force */, null);
        while (is.read() != -1) {
        }
        is.close();
      } catch (Exception e) {
        Log.e(TAG, e.getMessage());
      }
      return null;
    }
  }
}
