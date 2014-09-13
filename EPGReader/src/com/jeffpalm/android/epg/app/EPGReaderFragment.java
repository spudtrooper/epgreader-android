package com.jeffpalm.android.epg.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;

abstract class EPGReaderFragment extends Fragment implements OnActivityCreatedNotifier {

	private final DefaultOnActivityCreatedNotifier onCreateNotifier = new DefaultOnActivityCreatedNotifier();

	@Override
	public final void setListener(Listener listener) {
		onCreateNotifier.setListener(listener);
	}

	@Override
	public final void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		onActivityCreatedInternal(savedInstanceState);
		onCreateNotifier.notifyListener();
	}

	/**
	 * Subclasses should implement this instead of
	 * {@link #onActivityCreated(Bundle)}. This class will take care of calling
	 * the super implementation of {@link #onActivityCreated(Bundle)} and
	 * notifying the listener.
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void onActivityCreatedInternal(Bundle savedInstanceState);

}
