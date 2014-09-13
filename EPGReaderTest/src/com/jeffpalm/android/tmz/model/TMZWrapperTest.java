package com.jeffpalm.android.tmz.model;

import com.jeffpalm.android.tmz.model.TMZ;
import com.jeffpalm.android.tmz.model.TMZWrapper;

/**
 * Test case for {@code TMZAdapter}.
 */
public class TMZWrapperTest extends TMZTestCase {

  public void testGetSections() {
    TMZ tmz = getTMZ();
    TMZWrapper adapter = new TMZWrapper(tmz);
    assertTrue(adapter.getLinkItems().size() > 3);
    assertEquals(3, adapter.getSubSections().size());
  }

}
