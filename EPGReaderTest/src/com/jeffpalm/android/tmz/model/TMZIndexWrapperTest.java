package com.jeffpalm.android.tmz.model;

import com.jeffpalm.android.tmz.model.TMZ;
import com.jeffpalm.android.tmz.model.TMZIndexWrapper;

/**
 * Test case for {@code TMZIndexAdapter}.
 */
public class TMZIndexWrapperTest extends TMZTestCase {

  public void testGetSections() {
    TMZ tmz = getTMZ();
    TMZIndexWrapper adapter = new TMZIndexWrapper(tmz.getIndex());
    assertEquals(3, adapter.getSection("kboe5m_ate3w0_1fts2jn").getSections().size());
  }

}
