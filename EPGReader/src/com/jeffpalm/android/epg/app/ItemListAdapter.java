package com.jeffpalm.android.epg.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.imagedownloader.ImageDownloader;
import com.jeffpalm.android.epg.app.R;
import com.jeffpalm.android.tmz.model.TMZItem;
import com.jeffpalm.android.tmz.model.TMZLinkItem;
import com.jeffpalm.android.tmz.model.TMZSection;

/**
 * An adapter that holds a single TMZ instance and enumerates the child sections of the index and
 * link items.
 */
public class ItemListAdapter extends BaseAdapter {

	private final static String TAG = "TMZAdapter";

	private final Context mContext;
	private final ImageDownloader mImageDownloader;
	private LayoutInflater mLayoutInflater;

	private final Map<String, Boolean> isItemExpandedMap = new HashMap<String, Boolean>();

	private ArrayList<TMZItem<?>> tmzItems = new ArrayList<TMZItem<?>>();
	private String mSectionName;

	private final int mMaxTitleLines;
	private final int mMaxDescriptionLines;
	private final boolean mCanClickOnPhotos;

	interface TMZLinkItemHandler {
		/**
		 * called when the photo for the item is clicked.
		 * 
		 * @param item
		 */
		void onPhotoClicked(TMZLinkItem item);

		/**
		 * Called when a link item should be shown
		 * 
		 * @param item
		 */
		void onShowButtonClickedForLinkItem(TMZLinkItem item);
	}

	private TMZLinkItemHandler linkItemHandler;

	public void setHandler(TMZLinkItemHandler linkItemPhotoHandler) {
		this.linkItemHandler = linkItemPhotoHandler;
	}

	/**
	 * 
	 * @param context
	 * @param maxTitleLines max lines used in titles or -1 for no limit
	 * @param maxDescriptionLines max lines used in descriptions or -1 for no limit
	 */
	public ItemListAdapter(Context context, int maxTitleLines, int maxDescriptionLines,
			boolean canClickOnPhotos) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageDownloader = EPGReaderUtil.newImageDownloader();
		mMaxTitleLines = maxTitleLines;
		mMaxDescriptionLines = maxDescriptionLines;
		mCanClickOnPhotos = canClickOnPhotos;
	}

	/**
	 * Sets the section name, e.g. "Main", "PHOTOS", e.g.
	 * 
	 * @param sectionName the new section name
	 */
	public void setSectionName(String sectionName) {
		mSectionName = sectionName;
	}

	@Override
	public int getCount() {
		return tmzItems.size();
	}

	@Override
	public Object getItem(int position) {
		return tmzItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final TMZItem<?> item = (TMZItem<?>) tmzItems.get(position);

		if (item instanceof TMZLinkItem) {
			RelativeLayout itemView;
			if (convertView == null || convertView.getId() != R.layout.list_epg_link_item) {
				itemView = (RelativeLayout) mLayoutInflater.inflate(R.layout.list_epg_link_item, parent,
						false);
			} else {
				itemView = (RelativeLayout) convertView;
			}
			TMZLinkItem linkItem = (TMZLinkItem) item;
			View v = getLinkItemView(itemView, linkItem);
			if (shouldExpand(linkItem)) {
				expand(linkItem, v);
			} else {
				compress(linkItem, v);
			}
			return v;
		}

		if (item instanceof TMZSection) {
			RelativeLayout itemView;
			if (convertView == null || convertView.getId() != R.layout.list_epg_section) {
				itemView = (RelativeLayout) mLayoutInflater.inflate(R.layout.list_epg_section, parent,
						false);
			} else {
				itemView = (RelativeLayout) convertView;
			}
			return getSectionView(itemView, (TMZSection) item);
		}

		throw new RuntimeException("Item must be link item or section, not  " + item.getClass() + ":"
				+ item);
	}

	private View getSectionView(RelativeLayout itemView, final TMZSection item) {
		String title = item.getName();

		Log.v(TAG, "title=" + title);

		TextView titleText = (TextView) itemView.findViewById(R.id.listTitle);
		titleText.setText(title);

		return itemView;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private View getLinkItemView(RelativeLayout itemView, final TMZLinkItem linkItem) {
		String title = getName(itemView.getContext(), linkItem);
		String description = linkItem.getDescription();

		TextView titleText = (TextView) itemView.findViewById(R.id.listTitle);
		TextView descriptionText = (TextView) itemView.findViewById(R.id.listDescription);

		titleText.setText(title);

		if (description.trim().length() == 0) {
			// If there is no description text, clicking on the view shows the link.
			itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showLinkItem(linkItem);
				}
			});
			hideViewButton(itemView);
			descriptionText.setVisibility(View.GONE);
		} else {
			descriptionText.setText(description);
		}

		return itemView;
	}

	/**
	 * Returns the name of the item, possible prepending [VIDEO] or [PHOTOS].
	 */
	private String getName(Context context, TMZLinkItem item) {
		StringBuilder sb = new StringBuilder();
		if (item.isVideo()) {
			String videoPrefix = context.getString(R.string.video_prefix);
			// Don't prepend VIDEO if we're on the videos page.
			if (mSectionName == null || !mSectionName.toLowerCase().contains(videoPrefix.toLowerCase())) {
				sb.append("[");
				sb.append(videoPrefix);
				sb.append("] ");
			}
		} else if (item.isPhotos()) {
			String photosPrefix = context.getString(R.string.photos_prefix);
			// Don't prepend PHOTOS if we're on the photos page.
			// TODO(jpalm): Don't check "photo"
			if (mSectionName == null || !mSectionName.toLowerCase().contains("photo")) {
				sb.append("[");
				sb.append(photosPrefix);
				sb.append("] ");
			}
		}
		sb.append(item.getName());
		return sb.toString();
	}

	public void updateItems(List<TMZItem<?>> items) {
		tmzItems = new ArrayList<TMZItem<?>>(items);
		notifyDataSetChanged();
	}

	/**
	 * @return whether the link item should be expanded. Returns true for items without descriptions.
	 */
	private boolean shouldExpand(TMZLinkItem item) {
		return item.getDescription().length() > 0 && isExpanded(item);
	}

	/** @return whether item is expanded or not, according to the internal mapping. */
	private boolean isExpanded(TMZLinkItem item) {
		Boolean isExpanded = isItemExpandedMap.get(item.getId());
		if (isExpanded == null) {
			isExpanded = false;
		}
		return isExpanded;
	}

	void toggle(TMZLinkItem item, View v, int position) {
		if (isExpanded(item)) {
			compress(item, v);
		} else {
			expand(item, v);
		}
	}

	private void showPhoto(TMZLinkItem linkItem) {
		if (linkItemHandler != null) {
			linkItemHandler.onPhotoClicked(linkItem);
		}
	}

	private void showLinkItem(TMZLinkItem linkItem) {
		if (linkItemHandler != null) {
			linkItemHandler.onShowButtonClickedForLinkItem(linkItem);
		}
	}

	private void setImageVisible(ImageView imageView, final TMZLinkItem linkItem) {
		String imageUrl = linkItem.getThumbnail();
		mImageDownloader.download(imageUrl, imageView);
		imageView.setVisibility(View.VISIBLE);
		if (mCanClickOnPhotos) {
			// Clicking on the image opens a view of the photo
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showPhoto(linkItem);
				}
			});
		}
	}

	private void showViewButton(View itemView, final TMZLinkItem linkItem) {
		Button viewButton = (Button) itemView.findViewById(R.id.linkItemButtonView);
		viewButton.setVisibility(View.VISIBLE);
		viewButton.setText(getViewText(linkItem));
		viewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showLinkItem(linkItem);
			}
		});
	}

	private void hideViewButton(View itemView) {
		Button viewButton = (Button) itemView.findViewById(R.id.linkItemButtonView);
		viewButton.setVisibility(View.GONE);
		viewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Nothing
			}
		});
	}

	void expand(final TMZLinkItem linkItem, View itemView) {
		ImageView imageView = (ImageView) itemView.findViewById(R.id.listImage);
		TextView titleText = (TextView) itemView.findViewById(R.id.listTitle);
		TextView descriptionText = (TextView) itemView.findViewById(R.id.listDescription);

		setImageVisible(imageView, linkItem);

		titleText.setMaxLines(Integer.MAX_VALUE);
		descriptionText.setMaxLines(Integer.MAX_VALUE);
		isItemExpandedMap.put(linkItem.getId(), true);

		showViewButton(itemView, linkItem);
	}

	/**
	 * @param linkItem
	 * @return the resource id for the button to show the contents of the link item.
	 */
	private int getViewText(TMZLinkItem linkItem) {
		if (linkItem.isVideo()) {
			return R.string.view_video;
		}
		if (linkItem.isPhotos()) {
			return R.string.view_photos;
		}
		return R.string.view_story;
	}

	void compress(TMZLinkItem linkItem, View itemView) {
		ImageView imageView = (ImageView) itemView.findViewById(R.id.listImage);
		TextView titleText = (TextView) itemView.findViewById(R.id.listTitle);
		TextView descriptionText = (TextView) itemView.findViewById(R.id.listDescription);
		Button viewButton = (Button) itemView.findViewById(R.id.linkItemButtonView);

		setImageVisible(imageView, linkItem);
		if (viewButton != null) {
			viewButton.setVisibility(View.GONE);
		}

		if (mMaxTitleLines >= 0) {
			titleText.setMaxLines(mMaxTitleLines);
		}
		if (mMaxDescriptionLines >= 0) {
			descriptionText.setMaxLines(mMaxDescriptionLines);
		}
		isItemExpandedMap.put(linkItem.getId(), false);
	}

	public TMZItem<?> getTMZItem(int position) {
		return (TMZItem<?>) getItem(position);
	}
}
