package com.jeffpalm.android.tmz.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jeffpalm.android.epg.EPGItem;

/**
 * A helper class for extending a TMZFactory.
 */
public final class TMZFactoryHelper {

  /** @return a shared instance of {@link TMZFactoryHelper}. */
  public static TMZFactoryHelper getInstance() {
    return Holder.INSTANCE;
  }

  private final static class Holder {
    public final static TMZFactoryHelper INSTANCE = new TMZFactoryHelper();
  }

  /**
   * Adapts a collection of EPGItems to a list of TMZItems.
   * 
   * @return the adapted list of {@code TMZItem}
   */
  public List<TMZItem<?>> adaptAll(TMZAdapter factory, Collection<? extends EPGItem> items) {
    List<TMZItem<?>> result = new ArrayList<TMZItem<?>>(items.size());
    for (EPGItem item : items) {
      result.add(factory.adapt(item));
    }
    return result;
  }

  /**
   * Adapts a collection of EPGItems to a list of TMZItems into a type-safe
   * collection.
   * 
   * @return the adapted list of {@code TMZItem}
   */
  public <T extends TMZItem<?>> void adaptAll(TMZAdapter factory,
      Collection<? extends EPGItem> items, List<T> result, Class<T> cls) {
    for (EPGItem item : items) {
      result.add(cls.cast(factory.adapt(item)));
    }
  }
}
