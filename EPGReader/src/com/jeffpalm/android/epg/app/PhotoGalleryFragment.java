package com.jeffpalm.android.epg.app;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.TextView;

import com.jeffpalm.android.epg.app.R;
import com.jeffpalm.android.tmz.model.TMZContent;

/**
 * A fragment to show multiple photos.
 */
@SuppressWarnings("deprecation")
public class PhotoGalleryFragment extends EPGReaderFragment {

	public static final String TITLE = "title";
	public static final String CONTENT_ITEMS = "content.items";

	private UrlHandler urlHandler;

	interface UrlHandler {
		void show(String url);
	}

	public void setUrlHandler(UrlHandler urlHandler) {
		this.urlHandler = urlHandler;
	}

	@Override
	protected void onActivityCreatedInternal(Bundle savedInstanceState) {
		Bundle args = getArguments();
		String title = args.getString(TITLE);
		ArrayList<TMZContent> contentItems = args.getParcelableArrayList(CONTENT_ITEMS);

		TextView titleView = (TextView) getView().findViewById(R.id.titleText);
		titleView.setText(title);

		Gallery gallery = (Gallery) getView().findViewById(R.id.gallery);
		gallery.setSpacing(1);
		PhotoGalleryAdapter adapter = new PhotoGalleryAdapter(getActivity(), contentItems);
		adapter.setUrlHandler(new PhotoGalleryAdapter.UrlHandler() {
			@Override
			public void show(String url) {
				showUrl(url);
			}
		});
		gallery.setAdapter(adapter);
	}

	private void showUrl(String url) {
		if (urlHandler != null) {
			urlHandler.show(url);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
		return view;
	}
}
