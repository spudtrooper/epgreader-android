package com.jeffpalm.android.epg;

import android.os.Bundle;

import com.jeffpalm.android.epg.EPG;
import com.jeffpalm.android.epg.EPGSection;

/**
 * Test case for {@code EPGSection}.
 */

public class EPGSectionTest extends EPGItemTestCase {

  public void testParcelable() {
    doTestParcelable();
  }

  @Override protected void runTestParcelable(EPG epg) {
    EPGSection section = epg.getSections().get(0);
    Bundle bundle = new Bundle();
    String key = "key";
    bundle.putParcelable(key, section);
    EPGSection read = bundle.getParcelable(key);
    assertEquals(section, read);
  }
}
