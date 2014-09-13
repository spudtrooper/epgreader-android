package com.jeffpalm.android.tmz.model;

import android.os.Parcelable;

import com.jeffpalm.android.epg.EPGItem;

/**
 * Implements a full mapping from EPG nodes/items to TMZ node/items.
 */
public interface TMZAdapter extends Parcelable {

  /**
   * Converts an EPGItem to a TMZItem.
   * 
   * @return the TMZ item
   */
  TMZItem<?> adapt(EPGItem item);

}
