package com.jeffpalm.android.util;

import junit.framework.TestCase;

import com.jeffpalm.android.util.Util;

public class UtilTest extends TestCase {

  public void testGetParsingSafeString() {
    String input = "Elena Gant is in the middle of a war between Jeff Beecher and A&E because of her job as Mini Britney Spears, and a new reality show that’s trying to have her star.";
    String expected = "Elena Gant is in the middle of a war between Jeff Beecher and A&amp;E because of her job as Mini Britney Spears, and a new reality show that’s trying to have her star.";
    String have = Util.getParsingSafeString(input);
    assertEquals(expected, have);
  }
}
