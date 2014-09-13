package com.jeffpalm.android.util.urls;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.util.Log;

import com.jeffpalm.android.epg.app.BuildConfig;
import com.jeffpalm.android.util.Asserts;
import com.jeffpalm.android.util.Util;
import com.jeffpalm.android.util.urls.DatabaseHandler.CachedUrlResult;

public final class URLCache {

	private static final String TAG = "URLCache";

	final DatabaseHandler dbHandler;
	private final long cacheLifetimeMilis;
	private AtomicBoolean initted = new AtomicBoolean(false);

	public URLCache(Context context, long cacheLifetimeMilis) {
		this.dbHandler = new DatabaseHandler(context);
		this.cacheLifetimeMilis = cacheLifetimeMilis;
	}

	public void init() {
		if (!initted.getAndSet(true)) {
			dbHandler.open();
		}
	}

	/**
	 * Blocking get of the input stream for the url.
	 * 
	 * @param url URL from which to read
	 * @param force
	 * @param timeout timeout in millis, ignored if null
	 * @return input stream for the url
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public InputStream openStream(final String url, boolean force, Integer timeout)
			throws IllegalStateException, IOException {
		IllegalStateException caughtIllegalStateException = null;
		IOException caughtIOException = null;
		try {
			return openStreamInternal(url, force, timeout);
		} catch (IllegalStateException e) {
			caughtIllegalStateException = e;
		} catch (IOException e) {
			caughtIOException = e;
		}
		// If an exception occured and we were forcing the download, get it from the
		// cache.
		if (force) {
			Log.d(TAG, "Could not download, so getting from the cache.");
			return getInputStreamFromCache(url);
		}
		// Rethrow the original exception if we couldn't get a value from the cache.
		if (caughtIllegalStateException != null) {
			throw caughtIllegalStateException;
		}
		if (caughtIOException != null) {
			throw caughtIOException;
		}
		throw new RuntimeException("Couldn't download or retrieve " + url);
	}

	public InputStream openStreamInternal(final String url, boolean force, Integer timeout)
			throws IllegalStateException, IOException {
		init();
		Log.d(TAG, String.format("Looking up %s", url));
		if (!force) {
			InputStream inputStream = getInputStreamFromCache(url);
			if (inputStream != null) {
				return inputStream;
			}
		} else {
			Log.d(TAG, "Forcing download");
		}
		Log.d(TAG, "Opening stream");
		return new WriteToDatabaseOnCloseInputStream(url, Util.openStream(url, timeout), dbHandler);
	}

	private InputStream getInputStreamFromCache(String url) {
		DatabaseHandler.CachedUrlResult urlResult = dbHandler.getUrlResult(url);
		if (urlResult != null) {
			long diff = DatabaseHandler.now() - urlResult.getTimeMillis();
			Log.d(TAG, "Cached result updated " + diff + "ms ago cacheLifetimeMilis=" + cacheLifetimeMilis);
			if (diff < cacheLifetimeMilis) {
				Log.d(TAG, "Found a cached result");
				String body = urlResult.getBody();
				if (body != null) {
					return new StringInputStream(body);
				}
			} else {
				Log.d(TAG, "No cached result");
			}
		}
		return null;
	}

	public int getNumEntries() {
		init();
		return dbHandler.getNumEntries();
	}
}
