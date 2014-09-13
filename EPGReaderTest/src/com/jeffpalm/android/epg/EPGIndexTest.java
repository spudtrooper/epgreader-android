package com.jeffpalm.android.epg;

import android.os.Bundle;

import com.jeffpalm.android.epg.EPG;
import com.jeffpalm.android.epg.EPGIndex;

/**
 * Test case for {@code EPGIndex}.
 */
public class EPGIndexTest extends EPGItemTestCase {

  public void testParcelable() {
    doTestParcelable();
  }

  @Override protected void runTestParcelable(EPG epg) {
    EPGIndex index = epg.getEgpIndex();
    Bundle bundle = new Bundle();
    String key = "key";
    bundle.putParcelable(key, index);
    EPGIndex read = bundle.getParcelable(key);
    assertEquals(index, read);
  }
}
