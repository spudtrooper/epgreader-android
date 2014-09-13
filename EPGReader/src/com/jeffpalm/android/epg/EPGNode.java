package com.jeffpalm.android.epg;

/**
 * A node with a name in an EGP feed.
 */
public interface EPGNode extends EPGItem {

  /** Returns the id of this item. */
  String getId();

  /** Returns the readable name of this item. */
  String getName();
}
