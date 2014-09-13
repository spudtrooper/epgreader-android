package com.jeffpalm.android.epg;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.jeffpalm.android.epg.EPG;
import com.jeffpalm.android.epg.EPGParser;

public final class EPGTestUtil {

  private EPGTestUtil() {
  }

  /** File names of feeds. */
  public final static class Feeds {
    public final static String FULL = "tmz_feed.xml";
    public final static String PHOTO_GALLERY = "tmz_photoGallery.xml";
  }

  /**
   * @param context
   * @return an {@code EPG} instance for testing
   * @throws XmlPullParserException
   * @throws IOException
   */
  public static EPG getTestEPG(Context context) throws XmlPullParserException, IOException {
    EPGParser parser = new EPGParser();
    EPG epg = parser.parse(context.getAssets().open("tmz_feed.xml"));
    return epg;
  }

}
