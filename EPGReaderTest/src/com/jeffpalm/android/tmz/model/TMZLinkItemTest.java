package com.jeffpalm.android.tmz.model;

import android.os.Bundle;

import com.jeffpalm.android.tmz.model.TMZ;
import com.jeffpalm.android.tmz.model.TMZLinkItem;

/**
 * Test case for {@code TMZLinkItem}.
 */
public class TMZLinkItemTest extends TMZItemTestCase {
  public void testParcelable() {
    doTestParcelable();
  }

  @Override
  protected void runTestParcelable(TMZ tmz) {
    TMZLinkItem linkItem = tmz.getSection().getLinkItems().get(0);
    Bundle bundle = new Bundle();
    String key = "key";
    bundle.putParcelable(key, linkItem);
    TMZLinkItem read = bundle.getParcelable(key);
    assertEquals(linkItem, read);
  }
}
