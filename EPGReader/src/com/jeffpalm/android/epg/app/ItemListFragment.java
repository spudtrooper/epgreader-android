package com.jeffpalm.android.epg.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.jeffpalm.android.epg.app.ItemListAdapter.TMZLinkItemHandler;
import com.jeffpalm.android.epg.app.R;
import com.jeffpalm.android.tmz.model.TMZ;
import com.jeffpalm.android.tmz.model.TMZItem;
import com.jeffpalm.android.tmz.model.TMZLinkItem;
import com.jeffpalm.android.tmz.model.TMZSection;
import com.jeffpalm.android.tmz.model.TMZWrapper;
import com.jeffpalm.android.util.Asserts;

/**
 * A fragment with a list of TMZ items.
 */
public class ItemListFragment extends EPGReaderItemListFragment {

  private static final String TAG = "ItemListFragment";

  /**
   * The serialization (saved instance state) Bundle key representing the activated item position.
   * Only used on tablets.
   */
  private static final String STATE_ACTIVATED_POSITION = "activated_position";

  /** The current activated item position. Only used on tablets. */
  private int mActivatedPosition = ListView.INVALID_POSITION;

  public static final String URL = "url";
  public static final String SECTION_NAME = "section.name";
  public static final String CONFIG = "config";

  public static class Config implements Parcelable {

    private static final int MAX_LINES_TITLE = 3;
    private static final int MAX_LINES_DESCRIPTION = 3;

    /** The config for tablets. */
    public static final Config Tablet = new Config(MAX_LINES_TITLE, MAX_LINES_DESCRIPTION,
        true /* isExpandable */, false /* canClickOnPhotos */, true /* shouldSelectFirstItem */);

    /** The config for phones. */
    public static final Config Phone = new Config(MAX_LINES_TITLE, MAX_LINES_DESCRIPTION,
        true /* isExpandable */, false /* canClickOnPhotos */, false /* shouldSelectFirstItem */);

    private int maxTitleLines;
    private int maxDescriptionLines;
    private boolean isExpandable;
    private boolean canClickOnPhotos;
    private boolean shouldSelectFirstItem;

    public Config(Parcel in) {
      maxTitleLines = in.readInt();
      maxDescriptionLines = in.readInt();
      isExpandable = in.readByte() == 1;
      canClickOnPhotos = in.readByte() == 1;
      shouldSelectFirstItem = in.readByte() == 1;
    }

    private Config(int maxTitleLines, int maxDescriptionLines, boolean isExpandable,
        boolean canClickOnPhotos, boolean shouldSelectFirstItem) {
      this.maxTitleLines = maxTitleLines;
      this.maxDescriptionLines = maxDescriptionLines;
      this.isExpandable = isExpandable;
      this.canClickOnPhotos = canClickOnPhotos;
      this.shouldSelectFirstItem = shouldSelectFirstItem;
    }

    /** @return the max lines used in the title of items. */
    public int getMaxTitleLines() {
      return maxTitleLines;
    }

    /** @return the max lines used in the description of items. */
    public int getMaxDescriptionLines() {
      return maxDescriptionLines;
    }

    /**
     * @return whether the list items are expandable. If the aren't, clicking will select them.
     */
    public boolean isExpandable() {
      return isExpandable;
    }

    /** @return whether you can click on a photo in the list */
    public boolean canClickOnPhotos() {
      return canClickOnPhotos;
    }

    /**
     * @return whether this view should select the first item by default -- probably only on
     *         tablets.
     */
    public boolean shouldSelectFirstItem() {
      return shouldSelectFirstItem;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(maxTitleLines);
      dest.writeInt(maxDescriptionLines);
      dest.writeByte(byteValue(isExpandable));
      dest.writeByte(byteValue(canClickOnPhotos));
      dest.writeByte(byteValue(shouldSelectFirstItem));
    }

    private byte byteValue(boolean b) {
      return (byte) (b ? 1 : 0);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
      public Config createFromParcel(Parcel in) {
        return new Config(in);
      }

      public Config[] newArray(int size) {
        return new Config[size];
      }
    };
  }

  private String mUrl;
  private Callback mCallback;
  private ItemListAdapter mAdapter;
  private Config mConfig;
  private String mSectionName;

  private final static Map<Feature, String> FEATURES_TO_SECTION_NAMES = new HashMap<Feature, String>();
  static {
    FEATURES_TO_SECTION_NAMES.put(Feature.Photos, "photos");
    FEATURES_TO_SECTION_NAMES.put(Feature.TV, "tv");
    FEATURES_TO_SECTION_NAMES.put(Feature.Videos, "videos");
  }

  interface Callback {
    void onItemSelected(TMZItem<?> item);

    void showPhotoForLinkItem(TMZLinkItem item);

    void onSectionLoaded(String sectionName);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_item_list, container, false);
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Restore the previously serialized activated item position.
    if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
      setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
    }
  }

  @Override
  protected void onActivityCreatedInternal(Bundle savedState) {
    Activity activity = getActivity();
    Bundle args = getArguments();
    mConfig = args.getParcelable(CONFIG);
    mSectionName = args.getString(SECTION_NAME);

    mAdapter = new ItemListAdapter(activity, mConfig.getMaxTitleLines(),
        mConfig.getMaxDescriptionLines(), mConfig.canClickOnPhotos());
    mAdapter.setHandler(new TMZLinkItemHandler() {
      @Override
      public void onPhotoClicked(TMZLinkItem item) {
        showPhotoForLinkItem(item);
      }

      @Override
      public void onShowButtonClickedForLinkItem(TMZLinkItem item) {
        showLinkItemStory(item);
      }
    });
    setListAdapter(mAdapter);
    if (activity instanceof Callback) {
      mCallback = (Callback) activity;
    } else {
      throw new ClassCastException(activity.toString()
          + " must implemenet MyListFragment.OnItemSelectedListener");
    }

    // Open items on long press.
    getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

      @Override
      public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        TMZItem<?> item = mAdapter.getTMZItem(position);
        mCallback.onItemSelected(item);
        return true;
      }
    });

    String url = args.getString(URL);
    loadSection(activity, url);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mCallback = null;
  }

  @Override
  public void onPause() {
    super.onPause();
    getArguments().putParcelable(CONFIG, mConfig);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    TMZItem<?> item = mAdapter.getTMZItem(position);
    if (getConfig().isExpandable() && item instanceof TMZLinkItem) {
      toggle((TMZLinkItem) item, v, position);
    } else {
      mCallback.onItemSelected(item);
    }
  }

  private void showPhotoForLinkItem(TMZLinkItem item) {
    mCallback.showPhotoForLinkItem(item);
  }

  private void showLinkItemStory(TMZLinkItem item) {
    mCallback.onItemSelected(item);
  }

  private void toggle(TMZLinkItem item, View v, int position) {
    mAdapter.toggle(item, v, position);
  }

  public void loadSection(Context context, String sectionUrl) {
    Log.d(TAG, "loadSection " + sectionUrl);
    Asserts.assertNotNull(sectionUrl);
    Asserts.assertNotNull(context);
    loadUrl(context, sectionUrl, false /* force */);
  }

  /** Forces a reload of the current URL. */
  public void reloadCurrentUrl() {
    loadUrl(getActivity(), this.mUrl, true /* force */);
  }

  /** Navigates to the home list item. */
  public void navigateHome() {
    loadUrl(getActivity(), null, false /* force */);
  }

  /** @return the config after checking that it has been set. */
  private Config getConfig() {
    return Asserts.assertNotNull("Config must be set with setConfig", mConfig);
  }

  /**
   * Filters sections according to which features are enabled.
   */
  @SuppressLint("DefaultLocale")
  private List<TMZItem<?>> filterSetions(List<TMZSection> sections) {
    List<TMZItem<?>> filteredSections = new ArrayList<TMZItem<?>>();
    outer: for (TMZSection section : sections) {
      String name = section.getName().toLowerCase();
      for (Map.Entry<Feature, String> e : FEATURES_TO_SECTION_NAMES.entrySet()) {
        Feature feature = e.getKey();
        String sectionName = e.getValue();
        if (!feature.isEnabled() && name.contains(sectionName)) {
          // Skip a section that is not enabled
          continue outer;
        }
      }
      filteredSections.add(section);
    }
    return filteredSections;
  }

  /**
   * @param context
   * @param url nullable string url. If null we use the default from preferences.
   */
  private void loadUrl(Context context, String url, boolean force) {
    new FeedLoader(context, url, new FeedLoader.Processor() {
      @Override
      public void process(TMZ result) {
        TMZWrapper tmzWrapper = new TMZWrapper(result);
        List<TMZItem<?>> items = new ArrayList<TMZItem<?>>();
        items.addAll(filterSetions(tmzWrapper.getSubSections()));
        items.addAll(tmzWrapper.getLinkItems());
        mAdapter.setSectionName(mSectionName);
        mAdapter.updateItems(items);
        if (getConfig().shouldSelectFirstItem()) {
          selectFirstItem();
        }
        if (mCallback != null) {
          mCallback.onSectionLoaded(mSectionName);
        }
      }
    }, force).execute();
    this.mUrl = url;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    Intent prefsIntent = new Intent(getActivity().getApplicationContext(),
        EPGReaderPreferencesActivity.class);
    MenuItem preferences = menu.findItem(R.id.action_settings);
    preferences.setIntent(prefsIntent);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.action_settings:
      getActivity().startService(item.getIntent());
      break;
    }
    return true;
  }

  private void selectFirstItem() {
    int count = mAdapter.getCount();
    for (int i = 0; i < count; i++) {
      TMZItem<?> item = (TMZItem<?>) mAdapter.getItem(i);
      if (item instanceof TMZLinkItem) {
        TMZLinkItem linkItem = (TMZLinkItem) item;
        mCallback.onItemSelected(linkItem);
        break;
      }
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mActivatedPosition != ListView.INVALID_POSITION) {
      // Serialize and persist the activated item position.
      outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
    }
  }

  /**
   * Turns on activate-on-click mode. When this mode is on, list items will be given the 'activated'
   * state when touched.
   */
  public void setActivateOnItemClick(boolean activateOnItemClick) {
    // When setting CHOICE_MODE_SINGLE, ListView will automatically
    // give items the 'activated' state when touched.
    getListView().setChoiceMode(
        activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
  }

  private void setActivatedPosition(int position) {
    if (position == ListView.INVALID_POSITION) {
      getListView().setItemChecked(mActivatedPosition, false);
    } else {
      getListView().setItemChecked(position, true);
    }

    mActivatedPosition = position;
  }
}
