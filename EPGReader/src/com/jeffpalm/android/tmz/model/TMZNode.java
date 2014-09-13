package com.jeffpalm.android.tmz.model;

import com.jeffpalm.android.epg.EPGNode;

/**
 * 
 * @param <I>
 *          the underlying {@code EPGNode}
 */
public interface TMZNode<N extends EPGNode> extends TMZItem<N> {

  /** @return the underlying {@code EPGNode} */
  N getNode();
}
