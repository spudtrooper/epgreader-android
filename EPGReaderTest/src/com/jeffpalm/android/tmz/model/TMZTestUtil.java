package com.jeffpalm.android.tmz.model;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.jeffpalm.android.epg.EPGTestUtil;
import com.jeffpalm.android.tmz.model.TMZ;

/**
 * Utility methods for testing TMZ things.
 */
public final class TMZTestUtil {

  private TMZTestUtil() {
  }

  /**
   * @param context
   * @return an {@code EPG} for testing.
   * @throws IOException
   * @throws XmlPullParserException
   */
  public static TMZ getTMZ(Context context) throws XmlPullParserException, IOException {
    return new TMZ(EPGTestUtil.getTestEPG(context));
  }
}
