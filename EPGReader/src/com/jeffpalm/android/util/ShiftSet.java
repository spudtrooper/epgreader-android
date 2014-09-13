package com.jeffpalm.android.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of values with operations to take from the front and shift, take from the back and shift,
 * and peek a value.
 * 
 * @param <T> the type of the values stored
 */
public final class ShiftSet<T> {

  private final List<T> values = new ArrayList<T>();

  public ShiftSet() {
  }

  /**
   * Constructs a shift set using the values from {@code list}.
   * 
   * @param values source values
   */
  public ShiftSet(T... values) {
    addAll(values);
  }

  /**
   * Adds all values from {@code list} to {@code this}.
   * 
   * @param list source values
   */
  public void addAll(T... values) {
    if (values == null) {
      return;
    }
    for (T value : values) {
      if (!this.values.contains(value)) {
        this.values.add(value);
      }
    }
  }

  /**
   * Returns the values as a list.
   * 
   * @return the values as a list.
   */
  public List<T> asList() {
    return values;
  }

  /**
   * Gets the first value and shift that value to the end of the list.
   * 
   * @return the first value.
   */
  public T getFirst() {
    if (values.isEmpty()) {
      return null;
    }
    T value = values.remove(0);
    values.add(value);
    return value;
  }

  /**
   * Gets the last value and shift that value to the front of the list.
   * 
   * @return the first value.
   */

  public T getLast() {
    if (values.isEmpty()) {
      return null;
    }
    T value = values.remove(values.size() - 1);
    values.add(0, value);
    return value;
  }

  /**
   * Returns {@code true} if there are no values.
   * 
   * @return {@code true} if there are no values.
   */
  public boolean isEmpty() {
    return values.isEmpty();
  }

  /**
   * Returns the first value or {@code null} if there are no values.
   * 
   * @return the first value or {@code null} if there are no values.
   */
  public T peek() {
    return values.isEmpty() ? null : values.get(0);
  }

  public void addAll(List<T> values) {
    if (values == null) {
      return;
    }
    for (T value : values) {
      if (!this.values.contains(value)) {
        this.values.add(value);
      }
    }
  }
}
