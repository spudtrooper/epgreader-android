package com.jeffpalm.android.tmz.model;

import android.os.Bundle;

import com.jeffpalm.android.tmz.model.TMZ;

public class TMZTest extends TMZItemTestCase {
  public void testParcelable() {
    doTestParcelable();
  }

  @Override
  protected void runTestParcelable(TMZ tmz) {
    Bundle bundle = new Bundle();
    String key = "key";
    bundle.putParcelable(key, tmz);
    TMZ read = bundle.getParcelable(key);
    assertEquals(tmz, read);
  }
}
