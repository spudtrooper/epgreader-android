package com.jeffpalm.android.epg.app;

import com.jeffpalm.android.epg.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class EPGReaderPreferencesActivity extends PreferenceActivity {

  @SuppressWarnings("deprecation")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getPreferenceManager().setSharedPreferencesName(EPGReaderSharedPrefs.PREFS_NAME);
    addPreferencesFromResource(R.xml.preferences);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(Menu.NONE, 0, 0, "Show current settings");
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case 0:
      startActivity(new Intent(this, ItemListActivity.class));
      return true;
    }
    return false;
  }

}
