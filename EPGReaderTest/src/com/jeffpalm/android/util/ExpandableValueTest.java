package com.jeffpalm.android.util;

import junit.framework.TestCase;

import com.jeffpalm.android.util.ExpandableValue;

public class ExpandableValueTest extends TestCase {

  public void testCreate() {
  	ExpandableValue<String> value = ExpandableValue.create("hello");
  	assertEquals("hello", value.getValue());
  	assertEquals(false, value.isExpanded());
  }
  
  public void testToggle() {
  	ExpandableValue<String> value = ExpandableValue.create("hello");
  	assertEquals(false, value.isExpanded());
  	value.toggle();
  	assertEquals(true, value.isExpanded());
  	value.toggle();
  	assertEquals(false, value.isExpanded());
  }

}
