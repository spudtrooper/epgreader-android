package com.jeffpalm.android.epg.app;

import com.jeffpalm.android.epg.app.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class EPGReaderSharedPrefs {
	
	private EPGReaderSharedPrefs() {}

	public final static String PREFS_NAME = "egp.prefs";

	public static String getEpgUrl(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		return prefs.getString(context.getString(R.string.pref_epg_url), EPGReaderConstants.DATA_URL);
	}

	public static void setEpgUrl(Context context, String newValue) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		Editor prefsEditor = prefs.edit();
		prefsEditor.putString(context.getString(R.string.pref_epg_url), newValue);
		prefsEditor.commit();
	}
}
