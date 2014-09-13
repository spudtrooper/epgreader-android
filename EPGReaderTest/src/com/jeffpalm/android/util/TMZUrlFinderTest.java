package com.jeffpalm.android.util;

import java.io.IOException;

import junit.framework.TestCase;
import android.net.Uri;

import com.jeffpalm.android.epg.app.EPGReaderUrlFinder;

public class TMZUrlFinderTest extends TestCase {

	private EPGReaderUrlFinder finder;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		finder = new EPGReaderUrlFinder();
	}

	public void testGetPageUrl() throws IOException {
		String pageUrl = "http://www.tmz.com/2014/08/31/donald-sterling-spends-60th-anniversary-with-hated-wife/";
		Uri photoUri = finder.getPhotoUrl(pageUrl);
		Uri expectedPhotoUrl = Uri
				.parse("http://ll-media.tmz.com/2014/08/29/0829-donald-sterling-beach-tmz-6.jpg");
		assertEquals(expectedPhotoUrl, photoUri);
	}

	public void testGetVideoUrl() throws IOException {
		String videoPageUrl = "http://www.tmz.com/videos/0_35wig9pj";
		Uri videoUrl = finder.getVideoUri(videoPageUrl);
		Uri expectedVideoUrl = Uri
				.parse("http://tmz.vo.llnwd.net/o28/2014-05/04/0_35wig9pj_0_3yuavu7l_2.mp4");
		assertEquals(expectedVideoUrl, videoUrl);
	}

	public void testGetStoryUrl() throws IOException {
		String videoPageUrl = "http://www.tmz.com/2014/05/04/obama-president-wash-car-white-house-correspondents-dinner-secret-service/";
		Uri storyUrl = finder.getStoryUrl(videoPageUrl);
		Uri expectedVideoUrl = Uri
				.parse("http://tmz.vo.llnwd.net/o28/2014-05/04/0_n9b3q9pw_0_459x5boc_2.mp4");
		assertEquals(expectedVideoUrl, storyUrl);
	}
}
