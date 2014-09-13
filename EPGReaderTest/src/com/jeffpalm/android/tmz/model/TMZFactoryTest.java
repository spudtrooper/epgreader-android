package com.jeffpalm.android.tmz.model;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.jeffpalm.android.tmz.model.TMZ;
import com.jeffpalm.android.tmz.model.TMZFactory;

import android.test.ActivityTestCase;

/**
 * A test case class to testing TMZ things.
 */
abstract class TMZFactoryTest extends ActivityTestCase {
	
	public void testRead() throws IllegalStateException, XmlPullParserException, IOException {
		TMZFactory factory = new TMZFactory();
		String url = "http://tmz.rnmd.net/vsnaxepg/epgxml.do?carrier=TMZ";
		TMZ tmz = factory.read(getActivity(), url);
		assertNotNull(tmz);
	}
}
