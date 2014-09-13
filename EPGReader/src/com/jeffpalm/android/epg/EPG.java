package com.jeffpalm.android.epg;

import java.util.ArrayList;
import java.util.List;

/**
 * A POJO for the &lt;epg&gt; nodes.
 */
public class EPG {

  private final List<EPGSection> sections;
  private final EPGIndex epgIndex;

  private EPG(List<EPGSection> sections, EPGIndex epgIndex) {
    this.sections = sections;
    this.epgIndex = epgIndex;
  }

  public List<EPGSection> getSections() {
    return sections;
  }

  public EPGIndex getEgpIndex() {
    return epgIndex;
  }

  public static class Builder {
    private final List<EPGSection> sections = new ArrayList<EPGSection>();
    private EPGIndex epgIndex;

    private Builder() {
    }

    public Builder withSection(EPGSection section) {
      sections.add(section);
      return this;
    }

    public Builder withEpgIndex(EPGIndex epgIndex) {
      this.epgIndex = epgIndex;
      return this;
    }

    /** @return whether we have added an index yet */
    public boolean hasEpgIndex() {
      return epgIndex != null;
    }

    public EPG build() {
      return new EPG(sections, epgIndex);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
