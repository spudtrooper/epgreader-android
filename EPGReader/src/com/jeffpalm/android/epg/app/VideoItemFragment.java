package com.jeffpalm.android.epg.app;

import java.io.IOException;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * A fragment to show a title and video for a link item.
 */
public class VideoItemFragment extends Fragment implements MediaPlayer.OnCompletionListener,
		MediaPlayer.OnErrorListener, OnActivityCreatedNotifier {

	private static final String TAG = "VideoItemFragment";

	public static final String TITLE = "title";
	public static final String SHARE_URL = "share.url";

	private final DefaultOnActivityCreatedNotifier onActivityCreatedNotifier = new DefaultOnActivityCreatedNotifier();

	private VideoView mVideoView;
	private Integer mPosition;
	private Uri mVideoUri;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void setListener(Listener listener) {
		onActivityCreatedNotifier.setListener(listener);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle args = getArguments();
		String title = args.getString(TITLE);
		setTitle(title);
	}

	@Override
	public void onStart() {
		super.onStart();
		getVideoUrl();
	}

	private void getVideoUrl() {
		Bundle args = getArguments();
		String shareUrl = args.getString(SHARE_URL);
		getVideoUrl(shareUrl);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_video_item, container, false);
		mVideoView = (VideoView) view.findViewById(R.id.videoView);
		return view;
	}

	private void setTitle(String title) {
		TextView view = (TextView) getView().findViewById(R.id.titleText);
		view.setText(title);
	}

	private void getVideoUrl(String shareUrl) {
		AsyncTask<String, Integer, Uri> getVideoUrlTask = new AsyncTask<String, Integer, Uri>() {
			@Override
			protected Uri doInBackground(String... params) {
				String pageUrl = params[0];
				try {
					return new EPGReaderUrlFinder().getStoryUrl(pageUrl);
				} catch (IOException e) {
					Log.e(TAG, "Finding video url for " + pageUrl, e);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Uri result) {
				boolean shouldPlay = mVideoUri == null;
				mVideoUri = result;
				if (shouldPlay) {
					playVideo();
				}
			}
		};
		getVideoUrlTask.execute(shareUrl);
	}

	@Override
	public void onPause() {
		super.onPause();

		// Pause the video if it is playing
		if (mVideoView.isPlaying()) {
			mVideoView.pause();
		}

		// Save the current video position
		mPosition = mVideoView.getCurrentPosition();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mVideoUri == null) {
			getVideoUrl();
			return;
		}

		playVideo();
	}

	private void playVideo() {
		getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);

		mVideoView.setOnCompletionListener(this);
		mVideoView.setOnErrorListener(this);
		mVideoView.setKeepScreenOn(true);

		// Initialize the media controller
		MediaController mediaController = new MediaController(getActivity());
		// mediaController.setMediaPlayer(mVideoView);
		mediaController.setAnchorView(mVideoView);
		// Set-up the video view
		mVideoView.setMediaController(mediaController);
		mVideoView.setVideoPath(mVideoUri.toString());
		mVideoView.requestFocus();
		mVideoView.setVisibility(View.VISIBLE);

		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mVideoView.start();
				onActivityCreatedNotifier.notifyListener();
			}
		});

		if (mPosition != null) {
			// Restore the video position
			mVideoView.seekTo(mPosition);
			mVideoView.requestFocus();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Clean-up
		if (mVideoView != null) {
			mVideoView.stopPlayback();
			mVideoView = null;
		}
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		Log.e(TAG, "end video play");
	}

	@Override
	public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
		Log.e(TAG, "error: " + i);
		return true;
	}

}
