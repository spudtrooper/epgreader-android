package com.jeffpalm.android.tmz.model;

import android.os.Parcel;

import com.jeffpalm.android.epg.EPGItem;

/**
 * Holds an EPGItem and factory.
 * 
 * @param <N>
 *          type of the EPGItem
 */
abstract class AbstractTMZItem<I extends EPGItem> extends TMZFactoryHolder implements TMZItem<I> {

  protected AbstractTMZItem(TMZAdapter factory) {
    super(factory);
  }

  protected AbstractTMZItem(Parcel in) {
    super(in);
  }

  @Override public int hashCode() {
    return getItem().hashCode();
  }

  @Override public boolean equals(Object o) {
    if (!(o instanceof TMZItem)) {
      return false;
    }
    @SuppressWarnings("unchecked")
    TMZItem<I> that = (TMZItem<I>) o;
    return this.getItem().equals(that.getItem());
  }

}
