package com.jeffpalm.android.epg.app;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.jeffpalm.android.epg.app.R;
import com.jeffpalm.android.tmz.model.TMZContent;
import com.jeffpalm.android.tmz.model.TMZItem;
import com.jeffpalm.android.tmz.model.TMZLinkItem;
import com.jeffpalm.android.tmz.model.TMZSection;

public class ItemListActivity extends FragmentActivity implements ItemListFragment.Callback {
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
	 */
	private boolean mTwoPane;
	private HeaderFragment mHeaderFragment;
	private ItemListFragment mItemListFragment;
	private Timer mReloadTimer;

	private final static String TAG = "TMZSectionAndLinkItemActivity";

	public final static String EXTRA_SECTION_URL = "extra.section.url";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		// This has to be done first thing.
		if (findViewById(R.id.item_detail_container) != null) {
			mTwoPane = true;
		}
		
		mHeaderFragment = new HeaderFragment();
		addFragmentToTransaction(mHeaderFragment, R.id.header_fragment);

		mItemListFragment = new ItemListFragment();
		Bundle args = new Bundle();
		args.putString(ItemListFragment.URL, EPGReaderSharedPrefs.getEpgUrl(this));
		args.putString(ItemListFragment.SECTION_NAME, getResources().getString(R.string.main));
		args.putParcelable(ItemListFragment.CONFIG, determineConfig());
		mItemListFragment.setArguments(args);
		addFragmentToTransaction(mItemListFragment, R.id.item_list_fragment);

		mHeaderFragment.setReloadRunable(new Runnable() {
			public void run() {
				reloadCurrentUrl();
			}
		});
		mHeaderFragment.setTitleRunable(new Runnable() {
			public void run() {
				navigateToHome();
			}
		});

		if (mTwoPane) {
			// Only want to do landscape for tablets.
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			// The detail container view will be present only in the large-screen
			// layouts (res/values-large and res/values-sw600dp). If this view is
			// present, then the activity should be in two-pane mode.
			mTwoPane = true;

			NoContentFragment noContentFragment = new NoContentFragment();
			addFragmentToTransaction(noContentFragment, R.id.item_detail_container);
		}
	}

	private void reloadCurrentUrl() {
		if (mItemListFragment != null) {
			mHeaderFragment.startProgress();
			mItemListFragment.reloadCurrentUrl();
		}
	}

	private void loadSection(String sectionUrl) {
		mHeaderFragment.startProgress();
		mItemListFragment.loadSection(this, sectionUrl);
	}

	@Override
	protected void onResume() {
		super.onResume();
		String sectionUrl = getIntent().getStringExtra(EXTRA_SECTION_URL);
		if (sectionUrl == null) {
			sectionUrl = EPGReaderSharedPrefs.getEpgUrl(this);
		}
		loadSection(sectionUrl);
		mReloadTimer = new Timer();
		long reloadPeriod = EPGReaderConstants.URL_CACHE_LIFE_MILLIS;
		mReloadTimer.schedule(new ReloadTimerTask(), reloadPeriod, reloadPeriod);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mReloadTimer != null) {
			mReloadTimer.cancel();
			mReloadTimer = null;
		}
	}

	/**
	 * A TimerTask that reloads the item list fragment.
	 */
	private final class ReloadTimerTask extends TimerTask {

		@Override
		public void run() {
			reloadCurrentUrl();
		}

	}

	private void addFragmentToTransaction(Fragment fragment, int id) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(id, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private ItemListFragment.Config determineConfig() {
		return mTwoPane ? ItemListFragment.Config.Tablet : ItemListFragment.Config.Phone;
	}

	@Override
	public void onItemSelected(TMZItem<?> item) {
		if (item instanceof TMZLinkItem) {
			onLinkItemItemSelected((TMZLinkItem) item);
		} else if (item instanceof TMZSection) {
			onSectionSelected((TMZSection) item);
		} else {
			Log.d(TAG, "Cannot handle click on " + item);
		}
	}

	private String getPhotoUrlForLinkItem(TMZLinkItem linkItem) {
		for (TMZItem<?> item : linkItem.getContents()) {
			item.toString();
		}
		return linkItem.getThumbnail();
	}

	@Override
	public void showPhotoForLinkItem(final TMZLinkItem item) {
		PhotoItemFragment newFragment = new PhotoItemFragment();
		newFragment.setUrlHandler(new PhotoItemFragment.UrlHandler() {
			@Override
			public void show(String url) {
				onItemSelected(item);
			}
		});
		Bundle args = new Bundle();
		args.putString(PhotoItemFragment.TITLE, item.getName());
		args.putString(PhotoItemFragment.IMAGE_URL, getPhotoUrlForLinkItem(item));
		args.putString(PhotoItemFragment.SHARE_URL, item.getShareUrl());
		newFragment.setArguments(args);
		setFragment(newFragment);
	}

	@Override
	public void onSectionLoaded(String sectionName) {
		if (mHeaderFragment != null) {
			mHeaderFragment.setSubTitle(sectionName);
			mHeaderFragment.doneProgress();
		}
	}

	private void showVideoForLinkItem(TMZLinkItem item) {
		VideoItemFragment newFragment = new VideoItemFragment();
		Bundle args = new Bundle();
		args.putString(VideoItemFragment.TITLE, item.getName());
		args.putString(VideoItemFragment.SHARE_URL, item.getShareUrl());
		newFragment.setArguments(args);
		setFragment(newFragment);
	}

	private void showPhotosForLinkItem(TMZLinkItem item) {
		PhotoGalleryFragment newFragment = new PhotoGalleryFragment();
		Bundle args = new Bundle();
		ArrayList<TMZContent> contentItems = new ArrayList<TMZContent>();
		for (TMZItem<?> contentItem : item.getContents()) {
			if (contentItem instanceof TMZContent) {
				contentItems.add((TMZContent) contentItem);
			}
		}
		args.putString(PhotoGalleryFragment.TITLE, item.getName());
		args.putParcelableArrayList(PhotoGalleryFragment.CONTENT_ITEMS, contentItems);
		newFragment.setArguments(args);
		setFragment(newFragment);
	}

	private void showLinkForItem(TMZLinkItem item) {
		WebItemFragment newFragment = new WebItemFragment();
		Bundle args = new Bundle();
		args.putString(WebItemFragment.TITLE, item.getName());
		args.putString(WebItemFragment.URL, item.getShareUrl());
		newFragment.setArguments(args);
		setFragment(newFragment);
	}

	private String getHostFromDefaultUrl() {
		String url = EPGReaderSharedPrefs.getEpgUrl(this);

		int islashSlash = url.indexOf("//");
		if (islashSlash == -1) {
			return url;
		}
		int islash = url.indexOf("/", islashSlash + 2);
		if (islash == -1) {
			return url;
		}
		return url.substring(0, islash + 1);
	}

	private void onSectionSelected(TMZSection section) {
		String sectionUrl = getHostFromDefaultUrl() + section.getHref();
		navigateToUrl(sectionUrl, getSubtitleFromSectionName(section.getName()));
	}

	private void navigateToHome() {
		mHeaderFragment.startProgress();
		navigateToUrl(EPGReaderSharedPrefs.getEpgUrl(this), getResources().getString(R.string.main));
	}

	private void navigateToUrl(String url, String sectionName) {
		ItemListFragment newFragment = new ItemListFragment();
		Bundle args = new Bundle();
		args.putString(ItemListFragment.URL, url);
		args.putString(ItemListFragment.SECTION_NAME, sectionName);
		args.putParcelable(ItemListFragment.CONFIG, determineConfig());
		newFragment.setArguments(args);
		setFragment(newFragment, true /* forceReplaceItemList */);
		mHeaderFragment.setSubTitle(sectionName);
	}

	// TODO(jeff): Hack to use "Photos" instead of PHOTO GALLERY
	private String getSubtitleFromSectionName(String name) {
		if (name == null) {
			return "";
		}
		if (name.toLowerCase().contains("photo")) {
			return "Photos";
		}
		return name;
	}

	private void onLinkItemItemSelected(TMZLinkItem item) {
		if (item.isVideo()) {
			showVideoForLinkItem(item);
			return;
		}
		if (item.isPhotos()) {
			showPhotosForLinkItem(item);
			return;
		}
		showLinkForItem(item);
	}

	private void setFragment(Fragment newFragment) {
		setFragment(newFragment, false /* forceReplaceItemList */);
	}

	/**
	 * Transitions to the new fragment. If {@code forceReplaceItemList} is true, the item list is
	 * replaced, otherwise it depends on the size of the device.
	 * 
	 * @param newFragment the new fragment
	 * @param forceReplaceItemList whether to force the new fragment to replace the item list,
	 *          regardless of the size of the device
	 */
	private void setFragment(Fragment newFragment, boolean forceReplaceItemList) {
		mHeaderFragment.startProgress();
		OnActivityCreatedNotifier notifyingFragment = (OnActivityCreatedNotifier) newFragment;
		notifyingFragment.setListener(new OnActivityCreatedNotifier.Listener() {
			@Override
			public void onActivityCreatedDone() {
				mHeaderFragment.doneProgress();
			}
		});

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (!forceReplaceItemList && mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			transaction.replace(R.id.item_detail_container, newFragment);
		} else {
			// Replace whatever is in the item_list_fragment view with this fragment,
			// and add the transaction to the back stack so the user can navigate back
			transaction.replace(R.id.item_list_fragment, newFragment);
			transaction.addToBackStack(null);
		}
		// Commit the transaction
		transaction.commit();
	}
}
