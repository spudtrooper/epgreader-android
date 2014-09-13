package com.jeffpalm.android.tmz.model;

import android.os.Bundle;

import com.jeffpalm.android.tmz.model.TMZ;
import com.jeffpalm.android.tmz.model.TMZIndex;

/**
 * Test case for {@code TMZIndex}.
 */
public class TMZIndexTest extends TMZItemTestCase {
  public void testParcelable() {
    doTestParcelable();
  }

  @Override
  protected void runTestParcelable(TMZ tmz) {
    TMZIndex index = tmz.getIndex();
    Bundle bundle = new Bundle();
    String key = "key";
    bundle.putParcelable(key, index);
    TMZIndex read = bundle.getParcelable(key);
    assertEquals(index, read);
  }
}
