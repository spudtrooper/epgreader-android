package com.jeffpalm.android.epg;

import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A POJO for the <content> node.
 */
public final class EPGContent extends AbstractEPGItem implements EPGItem {

  private final String category;
  private final String name;
  private final String description;
  private final String thumbnail;
  private final String shareUrl;
  private final String url;
  private final String mediaType;
  private final String id;

  public static Builder builder() {
    return new Builder();
  }

  private EPGContent(String category, String name, String description, String thumbnail,
      String shareUrl, String url, String mediaType, String id) {
    this.category = category;
    this.name = name;
    this.description = description;
    this.thumbnail = thumbnail;
    this.shareUrl = shareUrl;
    this.url = url;
    this.mediaType = mediaType;
    this.id = id;
  }

  public boolean isVideo() {
    return mediaType != null && "video".equals(mediaType.toLowerCase(Locale.US));
  }

  public boolean isPhoto() {
    return mediaType != null && "image".equals(mediaType.toLowerCase(Locale.US));
  }

  public Object getId() {
    return id;
  }

  public String getCategory() {
    return category;
  }

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

  public String getMediaType() {
    return mediaType;
  }

  @Override
  public int hashCode() {
    return hashCode(category) + hashCode(name) + hashCode(description) + hashCode(thumbnail)
        + hashCode(shareUrl) + hashCode(url) + hashCode(mediaType) + hashCode(id);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof EPGContent)) {
      return false;
    }
    EPGContent that = (EPGContent) o;
    return equalsOrNull(this.category, that.category) && equalsOrNull(this.name, that.name)
        && equalsOrNull(this.description, that.description)
        && equalsOrNull(this.thumbnail, that.thumbnail)
        && equalsOrNull(this.shareUrl, that.shareUrl) && equalsOrNull(this.url, that.url)
        && equalsOrNull(this.mediaType, that.mediaType) && equalsOrNull(this.id, that.id);
  }

  public static class Builder {
    private String category;
    private String name;
    private String description;
    private String thumbnail;
    private String shareUrl;
    private String url;
    private String mediaType;
    private String id;

    public EPGContent build() {
      return new EPGContent(category, name, description, thumbnail, shareUrl, url, mediaType, id);
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

    public Builder withMediaType(String mediaType) {
      this.mediaType = mediaType;
      return this;
    }

    public Builder withId(String id) {
      this.id = id;
      return this;
    }
  }

  // -----------------------------------------------------------------------
  // Parcelable
  // -----------------------------------------------------------------------

  public EPGContent(Parcel in) {
    this.category = in.readString();
    this.name = in.readString();
    this.description = in.readString();
    this.thumbnail = in.readString();
    this.shareUrl = in.readString();
    this.url = in.readString();
    this.mediaType = in.readString();
    this.id = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(category);
    dest.writeString(name);
    dest.writeString(description);
    dest.writeString(thumbnail);
    dest.writeString(shareUrl);
    dest.writeString(url);
    dest.writeString(mediaType);
    dest.writeString(id);
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
