package com.jeffpalm.android.epg.app;

import com.jeffpalm.android.epg.app.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment that is a place-holder when there is no content.
 */
public class NoContentFragment extends EPGReaderFragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_no_content, container, false);
  }

  @Override
  protected void onActivityCreatedInternal(Bundle savedInstanceState) {
  }

}
