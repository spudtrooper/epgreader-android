package com.jeffpalm.android.tmz.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Holds a factory. Implements part of the {@code Parcelable} interface.
 */
abstract class TMZFactoryHolder implements Parcelable {

  private final TMZAdapter factory;

  TMZFactoryHolder(TMZAdapter factory) {
    this.factory = factory;
  }

  TMZFactoryHolder(Parcel in) {
    this((TMZAdapter) in.readParcelable(TMZContent.class.getClassLoader()));
  }

  /** Returns the factory to create TMZ items and nodes. */
  protected final TMZAdapter getFactory() {
    return factory;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public final void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(factory, flags);
    writeToParcelAfterFactory(dest, flags);
  }

  /** Subclasses should implement this to write the contents after the factory. */
  protected abstract void writeToParcelAfterFactory(Parcel dest, int flags);

}
