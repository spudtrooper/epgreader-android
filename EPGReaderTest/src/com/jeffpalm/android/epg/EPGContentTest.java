package com.jeffpalm.android.epg;

import android.os.Bundle;

import com.jeffpalm.android.epg.EPG;
import com.jeffpalm.android.epg.EPGContent;

/**
 * Test case for {@code EPGContent}.
 */
public class EPGContentTest extends EPGItemTestCase {

  public void testParcelable() {
    doTestParcelable();
  }

  @Override
  protected void runTestParcelable(EPG epg) {
    EPGContent content = epg.getSections().get(0).getLinkItems().get(0).getContents().get(0);
    Bundle bundle = new Bundle();
    String key = "key";
    bundle.putParcelable(key, content);
    EPGContent read = bundle.getParcelable(key);
    assertEquals(content, read);
  }
}
