package com.jeffpalm.android.epg;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A POJO for the <epg:index> node.
 */
public class EPGIndex extends AbstractEPGItem implements EPGItem {

	private final List<EPGSection> sections;

	private EPGIndex(List<EPGSection> sections) {
		this.sections = sections;
	}

	public List<EPGSection> getSections() {
		return sections;
	}

	public static class Builder {
		private final List<EPGSection> sections = new ArrayList<EPGSection>();

		private Builder() {
		}

		public Builder withSection(EPGSection section) {
			sections.add(section);
			return this;
		}

		public EPGIndex build() {
			return new EPGIndex(sections);
		}

	}

	@Override
	public int hashCode() {
		return sections.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof EPGIndex)) {
			return false;
		}
		EPGIndex that = (EPGIndex) o;
		return equalsOrNull(this.sections, that.sections);
	}

	public static Builder builder() {
		return new Builder();
	}

	// -----------------------------------------------------------------------
	// Parcelable
	// -----------------------------------------------------------------------

	public EPGIndex(Parcel in) {
		this.sections = new ArrayList<EPGSection>();
		in.readList(this.sections, EPGContent.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(sections);
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
