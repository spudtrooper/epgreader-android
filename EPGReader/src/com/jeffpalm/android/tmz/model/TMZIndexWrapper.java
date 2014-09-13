package com.jeffpalm.android.tmz.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to access a {@code TMZIndex}.
 */
public final class TMZIndexWrapper {

  private final TMZIndex index;

  public TMZIndexWrapper(TMZIndex index) {
    this.index = index;
  }

  /**
   * @param id ID of the section
   * @return TMZSection for the id
   */
  public TMZSection getSection(String id) {
    List<TMZSection> q = new ArrayList<TMZSection>();
    q.addAll(index.getSections());
    while (!q.isEmpty()) {
      TMZSection section = q.remove(0);
      if (id.equals(section.getId())) {
        return section;
      }
      q.addAll(section.getSections());
    }
    return null;
  }
}
