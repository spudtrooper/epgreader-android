package com.jeffpalm.android.epg;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * A POJO for the <section> node.
 */
public class EPGSection extends AbstractEPGNode implements EPGNode {

  private final static String TAG = "Section";

  private final List<EPGLinkItem> linkItems;
  private final List<EPGSection> sections;
  private final String name;
  private final String id;
  private final String href;
  private final Integer pageLength;

  private EPGSection(List<EPGLinkItem> linkItems, List<EPGSection> sections, String name,
      String id, String href, Integer pageLength) {
    this.linkItems = linkItems;
    this.sections = sections;
    this.name = name;
    this.id = id;
    this.href = href;
    this.pageLength = pageLength;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

  public Integer getPageLength() {
    return pageLength;
  }

  public String getHref() {
    return href;
  }

  public List<EPGLinkItem> getLinkItems() {
    return linkItems;
  }

  public List<EPGSection> getSections() {
    return sections;
  }

  @Override
  public int hashCode() {
    return hashCode(linkItems) + hashCode(sections) + hashCode(name) + hashCode(id)
        + hashCode(href) + hashCode(pageLength);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof EPGSection)) {
      return false;
    }
    EPGSection that = (EPGSection) o;
    return equalsOrNull(this.linkItems, that.linkItems)
        && equalsOrNull(this.sections, that.sections) && equalsOrNull(this.name, that.name)
        && equalsOrNull(this.id, that.id) && equalsOrNull(this.href, that.href)
        && equalsOrNull(this.pageLength, that.pageLength);
  }

  /** @return the builder for {@code EPGSection}s. */
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final List<EPGLinkItem> linkItems = new ArrayList<EPGLinkItem>();
    private final List<EPGSection> sections = new ArrayList<EPGSection>();
    private String id;
    private String name;
    private String href;
    private Integer pageLength;

    private Builder() {
    }

    public Builder withLinkItem(EPGLinkItem linkItem) {
      linkItems.add(linkItem);
      return this;
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withId(String id) {
      this.id = id;
      return this;
    }

    public Builder withHref(String href) {
      this.href = href;
      return this;
    }

    public Builder withSection(EPGSection section) {
      sections.add(section);
      return this;
    }

    public Builder withPageLength(String pagelength) {
      try {
        pageLength = Integer.parseInt(pagelength);
      } catch (NumberFormatException ignored) {
      }
      return this;
    }

    public EPGSection build() {
      return new EPGSection(linkItems, sections, name, id, href, pageLength);
    }

  }

  // -----------------------------------------------------------------------
  // Parcelable
  // -----------------------------------------------------------------------

  public EPGSection(Parcel in) {
    this.linkItems = new ArrayList<EPGLinkItem>();
    in.readList(this.linkItems, getClass().getClassLoader());
    this.sections = new ArrayList<EPGSection>();
    in.readList(this.sections, getClass().getClassLoader());
    this.name = in.readString();
    this.id = in.readString();
    this.href = in.readString();
    this.pageLength = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeList(linkItems);
    dest.writeList(sections);
    dest.writeString(name);
    dest.writeString(id);
    dest.writeString(href);
    dest.writeInt(pageLength == null ? -1 : pageLength);
  }

  @SuppressWarnings("rawtypes")
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public EPGContent createFromParcel(Parcel in) {
      return new EPGContent(in);
    }

    public EPGContent[] newArray(int size) {
      return new EPGContent[size];
    }
  };
}
