package com.jeffpalm.android.epg;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.test.ActivityTestCase;

import com.jeffpalm.android.epg.EPG;

/**
 * A test case class to testing {@code EPGItem}s.
 */
abstract class EPGItemTestCase extends ActivityTestCase {

  /** Test classes should call this do run tests on parcelling. */
  public void doTestParcelable() {
    runTestParcelable(getEPG());
  }

  /** @return an {@code EPG} for testing. */
  protected final EPG getEPG() {
    try {
      return EPGTestUtil.getTestEPG(getInstrumentation().getContext());
    } catch (XmlPullParserException e) {
      fail(e.getMessage());
    } catch (IOException e) {
      fail(e.getMessage());
    }
    throw new RuntimeException("The world has ended.");
  }

  /** Subclasses must implement this to test parcelling items. */
  protected abstract void runTestParcelable(EPG epg);
}
