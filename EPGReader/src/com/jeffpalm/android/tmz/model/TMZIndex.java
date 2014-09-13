package com.jeffpalm.android.tmz.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.jeffpalm.android.epg.EPGIndex;

/**
 * An index item in a TMZ feed.
 */
public final class TMZIndex extends AbstractTMZItem<EPGIndex> {

  private final EPGIndex index;

  public TMZIndex(TMZAdapter factory, EPGIndex index) {
    super(factory);
    this.index = index;
  }

  public TMZIndex(Parcel in) {
    super(in);
    this.index = in.readParcelable(TMZIndex.class.getClassLoader());
  }

  @Override public EPGIndex getItem() {
    return getIndex();
  }

  /** @return the underlying index. */
  public EPGIndex getIndex() {
    return index;
  }

  /** @return the list of TMZ sections */
  public List<TMZSection> getSections() {
    List<TMZSection> result = new ArrayList<TMZSection>();
    TMZFactoryHelper.getInstance().adaptAll(getFactory(), index.getSections(), result,
        TMZSection.class);
    return result;
  }

  @SuppressWarnings("rawtypes")
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public TMZIndex createFromParcel(Parcel in) {
      return new TMZIndex(in);
    }

    public TMZIndex[] newArray(int size) {
      return new TMZIndex[size];
    }
  };

  @Override protected void writeToParcelAfterFactory(Parcel dest, int flags) {
    dest.writeParcelable(index, flags);
  }

}
