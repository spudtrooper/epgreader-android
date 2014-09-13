package com.jeffpalm.android.epg;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A POJO for the <link_item> node.
 */
public final class EPGLinkItem extends AbstractEPGNode implements EPGNode {

	private final String category;
	private final String name;
	private final String description;
	private final String thumbnail;
	private final String shareUrl;
	private final String url;
	private final String id;
	private final List<EPGContent> contents;

	public static Builder builder() {
		return new Builder();
	}

	private EPGLinkItem(String category, String name, String description, String thumbnail,
			String shareUrl, String url, String id, List<EPGContent> contents) {
		this.category = category;
		this.name = name;
		this.description = description;
		this.thumbnail = thumbnail;
		this.shareUrl = shareUrl;
		this.url = url;
		this.id = id;
		this.contents = contents;
	}

	public List<EPGContent> getContents() {
		return contents;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getCategory() {
		return category;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public int hashCode() {
		return hashCode(category) + hashCode(name) + hashCode(description) + hashCode(thumbnail)
				+ hashCode(shareUrl) + hashCode(url) + hashCode(id) + hashCode(contents);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof EPGLinkItem)) {
			return false;
		}
		EPGLinkItem that = (EPGLinkItem) o;
		return equalsOrNull(this.category, that.category) && equalsOrNull(this.name, that.name)
				&& equalsOrNull(this.description, that.description)
				&& equalsOrNull(this.thumbnail, that.thumbnail)
				&& equalsOrNull(this.shareUrl, that.shareUrl) && equalsOrNull(this.url, that.url)
				&& equalsOrNull(this.id, that.id) && equalsOrNull(this.contents, that.contents);
	}

	public static class Builder {
		private String category;
		private String name;
		private String description;
		private String thumbnail;
		private String shareUrl;
		private String url;
		private String id;
		private List<EPGContent> contents = new ArrayList<EPGContent>();

		public EPGLinkItem build() {
			return new EPGLinkItem(category, name, description, thumbnail, shareUrl, url, id, contents);
		}

		public Builder withCategory(String category) {
			this.category = category;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder withThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
			return this;
		}

		public Builder withLink(String link) {
			this.shareUrl = link;
			return this;
		}

		public Builder withUrl(String url) {
			this.url = url;
			return this;
		}

		public Builder withId(String id) {
			this.id = id;
			return this;
		}

		public Builder withContent(EPGContent content) {
			contents.add(content);
			return this;
		}
	}

	// -----------------------------------------------------------------------
	// Parcelable
	// -----------------------------------------------------------------------

	public EPGLinkItem(Parcel in) {
		this.category = in.readString();
		this.name = in.readString();
		this.description = in.readString();
		this.thumbnail = in.readString();
		this.shareUrl = in.readString();
		this.url = in.readString();
		this.id = in.readString();
		this.contents = new ArrayList<EPGContent>();
		in.readList(contents, getClass().getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(category);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(thumbnail);
		dest.writeString(shareUrl);
		dest.writeString(url);
		dest.writeString(id);
		dest.writeList(contents);
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
