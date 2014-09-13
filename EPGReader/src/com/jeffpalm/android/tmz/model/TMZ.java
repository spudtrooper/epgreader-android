package com.jeffpalm.android.tmz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jeffpalm.android.epg.EPG;

/**
 * A TMZ feed.
 */
public class TMZ extends TMZFactoryHolder {

	private final TMZSection section;
	private final TMZIndex index;

	private static TMZSection getSection(TMZAdapter factory, EPG epg) {
		if (epg.getSections().isEmpty()) {
			throw new RuntimeException("No main section.");
		}
		if (epg.getSections().size() > 1) {
			throw new RuntimeException("There should only be one section.");
		}
		return (TMZSection) factory.adapt(epg.getSections().get(0));
	}

	private static TMZIndex getIndex(TMZAdapter factory, EPG epg) {
		return (TMZIndex) factory.adapt(epg.getEgpIndex());
	}

	public TMZ(EPG epg) {
		this(DefaultTMZAdapter.getInstance(), epg);
	}

	public TMZ(TMZAdapter factory, EPG epg) {
		this(factory, getSection(factory, epg), getIndex(factory, epg));
	}

	public TMZ(TMZAdapter factory, TMZSection section, TMZIndex index) {
		super(factory);
		this.section = section;
		this.index = index;
	}

	public TMZ(Parcel in) {
		this(DefaultTMZAdapter.getInstance(), (TMZSection) in
				.readParcelable(TMZ.class.getClassLoader()), (TMZIndex) in.readParcelable(TMZ.class
				.getClassLoader()));
	}

	/** @return the main TMZ section */
	public TMZSection getSection() {
		return section;
	}

	/** @return the TMZ index */
	public TMZIndex getIndex() {
		return index;
	}

	@Override
	public int hashCode() {
		return section.hashCode() + index.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TMZ)) {
			return false;
		}
		TMZ that = (TMZ) o;
		return this.section.equals(that.section) && this.index.equals(that.index);
	}

	@Override
	protected void writeToParcelAfterFactory(Parcel dest, int flags) {
		dest.writeParcelable(section, flags);
		dest.writeParcelable(index, flags);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public TMZ createFromParcel(Parcel in) {
			return new TMZ(in);
		}

		public TMZ[] newArray(int size) {
			return new TMZ[size];
		}
	};
}
