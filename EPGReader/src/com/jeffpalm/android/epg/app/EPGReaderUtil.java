package com.jeffpalm.android.epg.app;

import android.content.Context;

import com.example.android.imagedownloader.ImageDownloader;
import com.jeffpalm.android.util.urls.URLCache;

public final class EPGReaderUtil {

  private EPGReaderUtil() {
  }

  /**
   * Returns the mobile version of the link.
   * 
   * @param link TMZ link
   * @return mobile version of the link.
   */
  public static String getMobileLink(String link) {
    link = link.replace("www.", "m.");
    return link;
  }

  public static ImageDownloader newImageDownloader() {
    ImageDownloader imageDownloader = new ImageDownloader();
    imageDownloader.setMode(ImageDownloader.Mode.CORRECT);
    return imageDownloader;
  }

  /** @return a new URLCache for the app. */
  public static URLCache getURLCache(Context context) {
    return new URLCache(context, EPGReaderConstants.URL_CACHE_LIFE_MILLIS);
  }
}
