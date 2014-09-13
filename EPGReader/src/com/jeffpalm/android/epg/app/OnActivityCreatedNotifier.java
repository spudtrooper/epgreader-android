package com.jeffpalm.android.epg.app;

/**
 * Implementations of this interface should call the listener set by {@link #setListener(Listener)}
 * at the end of {@link Fragment#onActivityCreated}.
 */
public interface OnActivityCreatedNotifier {

  public interface Listener {
    /**
     * Called on the listener when {@link Fragment#onActivityCreated} has been called.
     */
    void onActivityCreatedDone();
  }

  void setListener(Listener listener);

}
