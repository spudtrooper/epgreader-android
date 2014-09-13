package com.jeffpalm.android.tmz.model;

import android.os.Bundle;

import com.jeffpalm.android.tmz.model.TMZ;
import com.jeffpalm.android.tmz.model.TMZSection;

/**
 * Test case for {@code TMZSection}.
 */
public class TMZSectionTest extends TMZItemTestCase {
  public void testParcelable() {
    doTestParcelable();
  }

  @Override protected void runTestParcelable(TMZ tmz) {
    TMZSection linkItem = tmz.getSection();
    Bundle bundle = new Bundle();
    String key = "key";
    bundle.putParcelable(key, linkItem);
    TMZSection read = bundle.getParcelable(key);
    assertEquals(linkItem, read);
  }
}
