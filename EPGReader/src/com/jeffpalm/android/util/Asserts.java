package com.jeffpalm.android.util;

import com.jeffpalm.android.epg.app.BuildConfig;

/**
 * Static methods for assertions.
 */
public final class Asserts {

  private Asserts() {
    throw new RuntimeException("Do not instantiate.");
  }

  /**
   * Returns value and throws an error if {@code value} is {@code null}.
   * 
   * @param value the value to check
   * @return value
   */
  public static <T> T assertNotNull(T value) {
    return assertNotNull("Value expected to be non-null", value);
  }

  /**
   * Returns value and throws an error if {@code value} is null.
   * 
   * @param msg error msg to use if {@code value} is {@code null}
   * @param value the value to check
   * @return value
   */
  public static <T> T assertNotNull(String msg, T value) {
    assertTrue(msg, value != null);
    return value;
  }

  /**
   * Asserts the value and throws an exception in debug mode with the message.
   * 
   * @param msg error message
   * @param value value to assert
   */
  public static void assertTrue(String msg, boolean value) {
    if (!value) {
      if (BuildConfig.DEBUG) {
        throw new RuntimeException(msg);
      } else {
        // TODO
      }
    }
  }

  /**
   * Asserts value with a default message.
   * 
   * @param value value to assert
   */
  public static void assertTrue(boolean value) {
    assertTrue("Value should be true", value);
  }
}
