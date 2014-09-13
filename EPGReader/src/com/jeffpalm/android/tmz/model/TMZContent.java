package com.jeffpalm.android.tmz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jeffpalm.android.epg.EPGContent;

/**
 * A content item in a TMZ feed.
 */
public final class TMZContent extends AbstractTMZItem<EPGContent> {

	private final EPGContent content;

	public TMZContent(TMZAdapter factory, EPGContent content) {
		super(factory);
		this.content = content;
	}

	public TMZContent(Parcel in) {
		super(in);
		this.content = (EPGContent) in.readParcelable(getClass().getClassLoader());
	}

	@Override
	public EPGContent getItem() {
		return getContent();
	}

	/** the underlying content */
	public EPGContent getContent() {
		return content;
	}

	public String getUrl() {
		return content.getUrl();
	}

	public String getShareUrl() {
		return content.getShareUrl();
	}

	public String getDescription() {
		return content.getDescription();
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public TMZContent createFromParcel(Parcel in) {
			return new TMZContent(in);
		}

		public TMZContent[] newArray(int size) {
			return new TMZContent[size];
		}
	};

	@Override
	protected void writeToParcelAfterFactory(Parcel dest, int flags) {
		dest.writeParcelable(content, flags);
	}

}
