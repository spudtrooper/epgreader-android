package com.jeffpalm.android.epg;

abstract class AbstractEPGItem implements EPGItem {

  @Override
  public int describeContents() {
    return 0;
  }

  /**
   * @param a the first object
   * @param b the second object
   * @return whether {@code a} and {@code b} are {@code null} or equal
   */
  protected final boolean equalsOrNull(Object a, Object b) {
    return a == null && b == null || a.equals(b);
  }

  /**
   * @param o
   * @return the {@code #hashCode()} of {@code o} of {@code 0} if {@code o} is {@code null}.
   */
  protected final int hashCode(Object o) {
    return o == null ? 0 : o.hashCode();
  }
}
