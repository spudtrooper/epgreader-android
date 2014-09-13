package com.jeffpalm.android.epg.app;

import java.io.IOException;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.imagedownloader.ImageDownloader;
import com.jeffpalm.android.epg.app.R;

/**
 * A fragment to show a title and photo for a link item.
 */
public class PhotoItemFragment extends EPGReaderFragment {

	public static final String TITLE = "title";
	public static final String IMAGE_URL = "image.url";
	public static final String SHARE_URL = "share.url";

	private static final String TAG = "PhotoItemFragment";

	private ImageDownloader imageDownloader;
	private UrlHandler urlHandler;

	interface UrlHandler {
		void show(String url);
	}

	public void setUrlHandler(UrlHandler urlHandler) {
		this.urlHandler = urlHandler;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		imageDownloader = EPGReaderUtil.newImageDownloader();
	}

	@Override
	protected void onActivityCreatedInternal(Bundle savedInstanceState) {
		Bundle args = getArguments();
		String title = args.getString(TITLE);
		String imageUrl = args.getString(IMAGE_URL);
		String shareUrl = args.getString(SHARE_URL);
		setTitle(title);
		setImageUrl(imageUrl);
		setupShareButton(shareUrl);
	}

	private void setupShareButton(final String shareUrl) {
		Button viewButton = (Button) getView().findViewById(R.id.buttonView);
		viewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showUrl(shareUrl);
			}
		});
	}

	private void showUrl(String url) {
		if (urlHandler != null) {
			urlHandler.show(url);
		}
	}

	private void setTitle(String title) {
		TextView view = (TextView) getView().findViewById(R.id.titleText);
		view.setText(title);
	}

	private void setImageUrl(String imageUrl) {
		ImageView view = (ImageView) getView().findViewById(R.id.imageView);
		imageDownloader.download(imageUrl, view);
	}

	@Override
	public void onStart() {
		super.onStart();
		// Replace the thumbnail URL with a nicer image
		Bundle args = getArguments();
		String shareUrl = args.getString(SHARE_URL);
		findNicerPhoto(shareUrl);
	}

	private void findNicerPhoto(final String shareUrl) {
		AsyncTask<String, Integer, Uri> getVideoUrlTask = new AsyncTask<String, Integer, Uri>() {
			@Override
			protected Uri doInBackground(String... params) {
				try {
					return new EPGReaderUrlFinder().getPhotoUrl(shareUrl);
				} catch (IOException e) {
					Log.e(TAG, "Finding photo url for " + shareUrl, e);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Uri result) {
				if (result != null) {
					setImageUrl(result.toString());
				}
			}
		};
		getVideoUrlTask.execute(shareUrl);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo_item, container, false);
		return view;
	}
}
