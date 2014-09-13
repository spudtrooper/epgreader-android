package com.jeffpalm.android.epg.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.jeffpalm.android.epg.EPG;
import com.jeffpalm.android.epg.EPGParser;
import com.jeffpalm.android.tmz.model.TMZ;
import com.jeffpalm.android.util.Asserts;
import com.jeffpalm.android.util.urls.URLCache;

/**
 * An {@code AsyncTask} that will load a feed and possibly cache it.
 */
final class FeedLoader extends AsyncTask<String, Void, TMZ> {

  private final static String TAG = "AbstractTMZLoader";

  /** An interface that processes the result of fetching and parsing a feed. */
  interface Processor {
    void process(TMZ result);
  }

  private final Processor processor;
  private final String url;
  private final boolean force;
  private final URLCache urlCache;

  FeedLoader(Context context, String url, Processor processor, boolean force) {
    this.url = Asserts.assertNotNull(url);
    this.processor = Asserts.assertNotNull(processor);
    this.force = force;
    this.urlCache = EPGReaderUtil.getURLCache(context);
  }

  @Override
  protected TMZ doInBackground(String... params) {
    EPGParser parser = new EPGParser();
    try {
      long start = SystemClock.currentThreadTimeMillis();
      EPG epg = parser.parse(urlCache.openStream(url, force, null));
      long stop = SystemClock.currentThreadTimeMillis();
      Log.d(TAG, "Downloaded and parsed EPG in " + (stop - start) + "ms");
      return new TMZ(epg);
    } catch (Throwable e) {
      Log.e(TAG, "doInBackground", e);
    }
    return null;
  }

  @Override
  protected void onPostExecute(TMZ result) {
    processor.process(result);
  }
}
