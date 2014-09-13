package com.jeffpalm.android.epg.app;

import com.jeffpalm.android.epg.app.OnActivityCreatedNotifier.Listener;

final class DefaultOnActivityCreatedNotifier {

	private Listener mListener;

	public void setListener(Listener listener) {
		mListener = listener;
	}

	public void notifyListener() {
		if (mListener != null) {
			mListener.onActivityCreatedDone();
		}
	}

}
