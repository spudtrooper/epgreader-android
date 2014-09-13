package com.jeffpalm.android.util;

import java.util.Arrays;

import junit.framework.TestCase;

import com.jeffpalm.android.util.ShiftSet;

public class ShiftSetTest extends TestCase {

  public void testAddAll() {
    ShiftSet<Integer> set = new ShiftSet<Integer>();
    set.addAll(1, 2, 3, 1, 2, 3);
    assertEquals(Arrays.asList(1, 2, 3), set.asList());
  }

  public void testIsEmpty_true() {
    ShiftSet<Integer> set = new ShiftSet<Integer>();
    assertTrue(set.isEmpty());
  }

  public void testIsEmpty_false() {
    ShiftSet<Integer> set = new ShiftSet<Integer>(1, 2, 3);
    assertFalse(set.isEmpty());
  }

  public void testPeek_null() {
    ShiftSet<Integer> set = new ShiftSet<Integer>();
    assertNull(set.peek());
  }

  public void testPeek() {
    ShiftSet<Integer> set = new ShiftSet<Integer>(1, 2, 3);
    assertEquals(Integer.valueOf(1), set.peek());
  }

  public void testGetFirst() {
    ShiftSet<Integer> set = new ShiftSet<Integer>(1, 2, 3);
    assertEquals(Integer.valueOf(1), set.getFirst());
    assertEquals(Integer.valueOf(2), set.getFirst());
    assertEquals(Integer.valueOf(3), set.getFirst());
    assertEquals(Integer.valueOf(1), set.getFirst());
    assertEquals(Integer.valueOf(2), set.getFirst());
    assertEquals(Integer.valueOf(3), set.getFirst());
  }

  public void testGetLast() {
    ShiftSet<Integer> set = new ShiftSet<Integer>(1, 2, 3);
    assertEquals(Integer.valueOf(3), set.getLast());
    assertEquals(Integer.valueOf(2), set.getLast());
    assertEquals(Integer.valueOf(1), set.getLast());
    assertEquals(Integer.valueOf(3), set.getLast());
    assertEquals(Integer.valueOf(2), set.getLast());
    assertEquals(Integer.valueOf(1), set.getLast());
  }
}
