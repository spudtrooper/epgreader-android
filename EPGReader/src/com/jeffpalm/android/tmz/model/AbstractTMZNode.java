package com.jeffpalm.android.tmz.model;

import android.os.Parcel;

import com.jeffpalm.android.epg.EPGNode;

/**
 * Holds an EPGNode and factory.
 * 
 * @param <N>
 *          type of the EPGNode
 */
abstract class AbstractTMZNode<N extends EPGNode> extends TMZFactoryHolder implements TMZNode<N> {

  protected AbstractTMZNode(TMZAdapter factory) {
    super(factory);
  }

  protected AbstractTMZNode(Parcel in) {
    super(in);
  }

  @Override public final N getItem() {
    return getNode();
  }

  /** @return the underlying id */
  public final String getId() {
    return getNode().getId();
  }

  /** @return the underlying name */
  public final String getName() {
    return getNode().getName();
  }
}
