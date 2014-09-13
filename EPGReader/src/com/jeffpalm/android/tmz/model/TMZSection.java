package com.jeffpalm.android.tmz.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.jeffpalm.android.epg.EPGSection;

/**
 * A section node in a TMZ feed.
 */
public final class TMZSection extends AbstractTMZNode<EPGSection> {

  private final EPGSection section;

  public TMZSection(TMZAdapter factory, EPGSection section) {
    super(factory);
    this.section = section;
  }

  public TMZSection(Parcel in) {
    super(in);
    this.section = (EPGSection) in.readParcelable(getClass().getClassLoader());
  }

  @Override
  public EPGSection getNode() {
    return getSection();
  }

  /** @return the underlying section */
  public EPGSection getSection() {
    return section;
  }

  /** @return the underlying href. */
  public String getHref() {
    return section.getHref();
  }

  /** @return the list of TMZ sections */
  public List<TMZSection> getSections() {
    List<TMZSection> result = new ArrayList<TMZSection>();
    TMZFactoryHelper.getInstance().adaptAll(getFactory(), section.getSections(), result,
        TMZSection.class);
    return result;
  }

  /** @return the list of TMZ link items */
  public List<TMZLinkItem> getLinkItems() {
    List<TMZLinkItem> result = new ArrayList<TMZLinkItem>();
    TMZFactoryHelper.getInstance().adaptAll(getFactory(), section.getLinkItems(), result,
        TMZLinkItem.class);
    return result;
  }

  /** @return the list of TMZ sections and link items */
  public List<TMZItem<?>> getAllItems() {
    List<TMZItem<?>> items = new ArrayList<TMZItem<?>>();
    items.addAll(getSections());
    items.addAll(getLinkItems());
    return items;
  }

  @SuppressWarnings("rawtypes")
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public TMZSection createFromParcel(Parcel in) {
      return new TMZSection(in);
    }

    public TMZSection[] newArray(int size) {
      return new TMZSection[size];
    }
  };

  @Override
  protected void writeToParcelAfterFactory(Parcel dest, int flags) {
    dest.writeParcelable(section, flags);
  }

}
