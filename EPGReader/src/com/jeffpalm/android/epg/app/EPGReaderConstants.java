package com.jeffpalm.android.epg.app;

public final class EPGReaderConstants {
  private EPGReaderConstants() {
  }

  public final static String DATA_URL = "http://tmz.rnmd.net/vsnaxepg/epgxml.do?carrier=TMZ&v=2";

  /** The URL's lifetime in the cache in milliseconds. */
  public static final long URL_CACHE_LIFE_MILLIS = 10 * 60 * 1000;

}
