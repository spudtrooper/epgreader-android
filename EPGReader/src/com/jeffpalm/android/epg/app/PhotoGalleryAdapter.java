package com.jeffpalm.android.epg.app;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.imagedownloader.ImageDownloader;
import com.jeffpalm.android.epg.app.R;
import com.jeffpalm.android.tmz.model.TMZContent;

/**
 * An adapter around an array of TMZContent items.
 */
final class PhotoGalleryAdapter extends BaseAdapter {

  private final Context mContext;
  private final List<TMZContent> mContentItems;
  private final ImageDownloader mImageDownloader;
  private final LayoutInflater mLayoutInflater;

  private UrlHandler urlHandler;

  interface UrlHandler {
    void show(String url);
  }

  PhotoGalleryAdapter(Context context, List<TMZContent> contentItems) {
    mContext = context;
    mContentItems = contentItems;
    mImageDownloader = EPGReaderUtil.newImageDownloader();
    mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return mContentItems.size();
  }

  @Override
  public Object getItem(int position) {
    return getContentItem(position);
  }

  private TMZContent getContentItem(int position) {
    return mContentItems.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    final TMZContent content = getContentItem(position);

    ImageView imageView;
    if (convertView == null || convertView.getId() != R.layout.photo_gallery_item) {
      imageView = (ImageView) mLayoutInflater.inflate(R.layout.photo_gallery_item, parent, false);
    } else {
      imageView = (ImageView) convertView;
    }
    mImageDownloader.download(content.getUrl(), imageView);
    imageView.setVisibility(View.VISIBLE);
    // imageView.setBackgroundResource(android.R.color.black);

    return imageView;
  }

  public void setUrlHandler(UrlHandler urlHandler) {
    this.urlHandler = urlHandler;
  }

  @SuppressWarnings("unused")
  private void showUrl(String url) {
    if (urlHandler != null) {
      urlHandler.show(url);
    }
  }

}
