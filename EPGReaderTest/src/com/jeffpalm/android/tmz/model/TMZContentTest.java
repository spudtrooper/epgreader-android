package com.jeffpalm.android.tmz.model;

import android.os.Bundle;

import com.jeffpalm.android.tmz.model.TMZ;
import com.jeffpalm.android.tmz.model.TMZContent;

/**
 * Test case for {@code TMZContent}.
 */
public class TMZContentTest extends TMZItemTestCase {
  public void testParcelable() {
    doTestParcelable();
  }

  @Override protected void runTestParcelable(TMZ tmz) {
    TMZContent content = (TMZContent) tmz.getSection().getLinkItems().get(0).getContents().get(0);
    Bundle bundle = new Bundle();
    String key = "key";
    bundle.putParcelable(key, content);
    TMZContent read = bundle.getParcelable(key);
    assertEquals(content, read);
  }
}
