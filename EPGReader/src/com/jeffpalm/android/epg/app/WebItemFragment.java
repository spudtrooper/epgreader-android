package com.jeffpalm.android.epg.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A fragment to show a title and photo for a link item.
 */
public class WebItemFragment extends Fragment implements OnActivityCreatedNotifier {

	private static final String TAG = "WebItemFragment";

	public static final String TITLE = "title";
	public static final String URL = "url";

	private final DefaultOnActivityCreatedNotifier onActivityCreatedNotifier = new DefaultOnActivityCreatedNotifier();

	@Override
	public void setListener(Listener listener) {
		onActivityCreatedNotifier.setListener(listener);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle args = getArguments();
		String url = args.getString(URL);
		setUrl(url);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_web_item, container, false);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setUrl(final String url) {
		Log.d(TAG, "setUrl url=" + url);
		final WebView view = (WebView) getView().findViewById(R.id.webView);
		view.getSettings().setBuiltInZoomControls(true);
		view.getSettings().setJavaScriptEnabled(true);
		// Set the custom web client to prevent the page from opening into the
		// browser.
		// http://stackoverflow.com/questions/13321510/prevent-webview-from-opening-the-browser
		view.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						view.loadUrl(url);
					}
				});
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Log.d(TAG, "onPageFinished");
				onActivityCreatedNotifier.notifyListener();
			}
		});
		view.loadUrl(url);
	}

}
