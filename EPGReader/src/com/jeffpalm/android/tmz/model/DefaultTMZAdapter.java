package com.jeffpalm.android.tmz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jeffpalm.android.epg.EPGContent;
import com.jeffpalm.android.epg.EPGIndex;
import com.jeffpalm.android.epg.EPGItem;
import com.jeffpalm.android.epg.EPGLinkItem;
import com.jeffpalm.android.epg.EPGSection;

/**
 * Default implementation of {@link TMZAdapter}.
 */
public final class DefaultTMZAdapter implements TMZAdapter {

	/** @return a shared instance of {@link DefaultTMZAdapter}. */
	public static DefaultTMZAdapter getInstance() {
		return Holder.INSTANCE;
	}

	public DefaultTMZAdapter(Parcel in) {

	}

	public DefaultTMZAdapter() {

	}

	private final static class Holder {
		public final static DefaultTMZAdapter INSTANCE = new DefaultTMZAdapter();
	}

	@Override
	public TMZItem<?> adapt(EPGItem item) {
		if (item instanceof EPGLinkItem) {
			return new TMZLinkItem(this, (EPGLinkItem) item);
		}
		if (item instanceof EPGSection) {
			return new TMZSection(this, (EPGSection) item);
		}
		if (item instanceof EPGContent) {
			return new TMZContent(this, (EPGContent) item);
		}
		if (item instanceof EPGIndex) {
			return new TMZIndex(this, (EPGIndex) item);
		}
		throw new RuntimeException("Cannot convert item of class " + item.getClass());
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public DefaultTMZAdapter createFromParcel(Parcel in) {
			return new DefaultTMZAdapter(in);
		}

		public DefaultTMZAdapter[] newArray(int size) {
			return new DefaultTMZAdapter[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// Nothing, there is no state
	}
}
