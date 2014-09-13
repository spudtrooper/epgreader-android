package com.jeffpalm.android.epg.app;

import com.jeffpalm.android.epg.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;

/**
 * The header bar, which contains the title, sub title, reload button, and progress spinny.
 */
public final class HeaderFragment extends Fragment {

	private static final String TAG = "HeaderFragment";

	private TextView mTitleText;
	private TextView mSubTitleText;
	private ProgressBar mProgressBar;
	private ImageView mReloadButton;

	/**
	 * Stores whether we should be showing progress or not. We need this because we could call
	 * startProgress and doneProgress before the activity is attached.
	 */
	private boolean mInProgress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.header, container, false);
		mTitleText = (TextView) view.findViewById(R.id.headerTitle);
		mSubTitleText = (TextView) view.findViewById(R.id.headerSubTitle);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		mReloadButton = (ImageView) view.findViewById(R.id.reloadButton);
		mSubTitleText.setText("");

		ImageView settingsButton = (ImageView) view.findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent prefsIntent = new Intent(getActivity().getApplicationContext(),
						EPGReaderPreferencesActivity.class);
				getActivity().startActivity(prefsIntent);
			}
		});

		setReloadOnClickListener();
		setTitleTextOnClickListener();

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Call startProgress again if we've called it before the activity we attached.
		if (mInProgress) {
			startProgress();
		}
	}

	/**
	 * Sets the subtitle, which is empty by default.
	 * 
	 * @param subTitle the new sub title
	 */
	public void setSubTitle(CharSequence subTitle) {
		mSubTitleText.setText(subTitle);
	}

	/** Starts the progress bar spinning and hides the reload button. */
	public void startProgress() {
		Log.d(TAG, "startProgress");
		mInProgress = true;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mReloadButton.setVisibility(View.GONE);
					mProgressBar.setIndeterminate(true);
					mProgressBar.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	/** Stops the progress bar from spinning and shows the reload button. */
	public void doneProgress() {
		Log.d(TAG, "doneProgress");
		mInProgress = false;
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mProgressBar.setIndeterminate(false);
					mProgressBar.setVisibility(View.GONE);
					mReloadButton.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	private Runnable mReloadButtonOnClickRunnable;
	private Runnable mTitleTextOnClickRunnable;

	/**
	 * Specifies that {@code runnable} should be run when the reload button is clicked.
	 * 
	 * @param runnable The runnable to run when the reload button is clicked.
	 */
	public void setReloadRunable(final Runnable runnable) {
		mReloadButtonOnClickRunnable = runnable;
		setReloadOnClickListener();
	}

	/**
	 * Sets the onclick listener for {@link #mReloadButton} if both
	 * {@link #mReloadButtonOnClickRunnable} and the runnable set by {@link #setReloadRunable} have
	 * been set.
	 */
	private void setReloadOnClickListener() {
		if (mTitleText != null && mTitleTextOnClickRunnable != null) {
			mReloadButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mReloadButtonOnClickRunnable.run();
				}
			});
		}
	}

	/**
	 * Specifies that {@code runnable} should be run when the title button is clicked.
	 * 
	 * @param runnable The runnable to run when the title button is clicked.
	 */
	public void setTitleRunable(final Runnable runnable) {
		mTitleTextOnClickRunnable = runnable;
		setTitleTextOnClickListener();
	}

	/**
	 * Sets the onclick listener for {@link #mTitleText} if both {@link #mTitleText} and the runnable
	 * set by {@link #setTitleTextRunable} have been set.
	 */
	private void setTitleTextOnClickListener() {
		if (mTitleText != null && mTitleTextOnClickRunnable != null) {
			mTitleText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mTitleTextOnClickRunnable.run();
				}
			});
		}
	}
}
