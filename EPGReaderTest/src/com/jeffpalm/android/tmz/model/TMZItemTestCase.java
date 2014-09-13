package com.jeffpalm.android.tmz.model;

import com.jeffpalm.android.tmz.model.TMZ;

/**
 * A test case class to testing {@code TMZItem}s.
 */
abstract class TMZItemTestCase extends TMZTestCase {

  /** Test classes should call this do run tests on parcelling. */
  public void doTestParcelable() {
    runTestParcelable(getTMZ());
  }

  /** Subclasses must implement this to test parcelling items. */
  protected abstract void runTestParcelable(TMZ tmz);
}
