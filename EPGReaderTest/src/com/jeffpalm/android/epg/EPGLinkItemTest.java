package com.jeffpalm.android.epg;

import android.os.Bundle;

import com.jeffpalm.android.epg.EPG;
import com.jeffpalm.android.epg.EPGLinkItem;

/**
 * Test case for {@code EPGLinkItem}.
 */

public class EPGLinkItemTest extends EPGItemTestCase {

  public void testParcelable() {
    doTestParcelable();
  }

  @Override protected void runTestParcelable(EPG epg) {
    EPGLinkItem linkItem = epg.getSections().get(0).getLinkItems().get(0);
    Bundle bundle = new Bundle();
    String key = "key";
    bundle.putParcelable(key, linkItem);
    EPGLinkItem read = bundle.getParcelable(key);
    assertEquals(linkItem, read);
  }
}
