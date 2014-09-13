package com.jeffpalm.android.tmz.model;

import android.os.Parcelable;

import com.jeffpalm.android.epg.EPGItem;

/**
 * 
 * @param <I>
 *          the underlying {@code EPGItem}
 */
public interface TMZItem<I extends EPGItem> extends Parcelable {

  /** @return the underlying {@code EPGItem} */
  I getItem();
}
