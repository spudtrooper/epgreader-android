package com.jeffpalm.android.tmz.model;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.test.ActivityTestCase;

import com.jeffpalm.android.tmz.model.TMZ;

/**
 * A test case class to testing TMZ things.
 */
abstract class TMZTestCase extends ActivityTestCase {

  /** @return an {@code EPG} for testing. */
  protected final TMZ getTMZ() {
    try {
      return TMZTestUtil.getTMZ(getInstrumentation().getContext());
    } catch (XmlPullParserException e) {
      fail(e.getMessage());
    } catch (IOException e) {
      fail(e.getMessage());
    }
    throw new RuntimeException("The world has ended.");
  }
}
