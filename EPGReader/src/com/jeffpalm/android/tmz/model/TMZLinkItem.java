package com.jeffpalm.android.tmz.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.jeffpalm.android.epg.EPGContent;
import com.jeffpalm.android.epg.EPGLinkItem;

/**
 * A link item node in a TMZ feed.
 */
public final class TMZLinkItem extends AbstractTMZNode<EPGLinkItem> {

	private final EPGLinkItem linkItem;

	public TMZLinkItem(TMZAdapter factory, EPGLinkItem section) {
		super(factory);
		this.linkItem = section;
	}

	public TMZLinkItem(Parcel in) {
		super(in);
		this.linkItem = (EPGLinkItem) in
				.readParcelable(getClass().getClassLoader());
	}

	@Override
	public EPGLinkItem getNode() {
		return getLinkItem();
	}

	/** @return the underlying link item */
	public EPGLinkItem getLinkItem() {
		return linkItem;
	}

	/** @return the list of TMZ content items */
	public List<TMZItem<?>> getContents() {
		return TMZFactoryHelper.getInstance().adaptAll(getFactory(),
				linkItem.getContents());
	}

	/** @return the underlying thumb nail */
	public String getThumbnail() {
		return linkItem.getThumbnail();
	}

	/** @return the underlying description */
	public String getDescription() {
		return linkItem.getDescription();
	}

	// TODO(jeff): Needs test
	/** @return whether this node is a video link. */
	public boolean isVideo() {
		for (EPGContent c : linkItem.getContents()) {
			if (c.isVideo()) {
				return true;
			}
		}
		return false;
	}

	// TODO(jeff): Needs test
	/** @return whether this node contains all photo links. */
	public boolean isPhotos() {
		for (EPGContent c : linkItem.getContents()) {
			if (!c.isPhoto()) {
				return false;
			}
		}
		return true;
	}

	/** @return the underlying share url */
	public String getShareUrl() {
		return linkItem.getShareUrl();
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public TMZLinkItem createFromParcel(Parcel in) {
			return new TMZLinkItem(in);
		}

		public TMZLinkItem[] newArray(int size) {
			return new TMZLinkItem[size];
		}
	};

	@Override
	protected void writeToParcelAfterFactory(Parcel dest, int flags) {
		dest.writeParcelable(linkItem, flags);
	}

}
