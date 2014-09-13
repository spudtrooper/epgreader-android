package com.jeffpalm.android.epg;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.test.ActivityTestCase;

import com.jeffpalm.android.epg.EPG;
import com.jeffpalm.android.epg.EPGContent;
import com.jeffpalm.android.epg.EPGIndex;
import com.jeffpalm.android.epg.EPGLinkItem;
import com.jeffpalm.android.epg.EPGParser;
import com.jeffpalm.android.epg.EPGSection;

public class EPGParserTest extends ActivityTestCase {

  public void testParse_photoGallery() throws IOException, XmlPullParserException {
    EPGParser parser = new EPGParser();
    EPG epg = parser.parse(getInstrumentation().getContext().getAssets()
        .open(EPGTestUtil.Feeds.PHOTO_GALLERY));

    assertTrue(epg.getSections().size() > 0);

    EPGSection photoGallery = epg.getSections().get(0);

    assertEquals(28, photoGallery.getLinkItems().size());

    EPGIndex index = epg.getEgpIndex();
    assertNotNull(index);
    assertEquals(1, index.getSections().size());
  }

  public void testParse_full() throws IOException, XmlPullParserException {
    EPGParser parser = new EPGParser();
    EPG epg = parser.parse(getInstrumentation().getContext().getAssets()
        .open(EPGTestUtil.Feeds.FULL));

    assertTrue(epg.getSections().size() > 0);

    EPGSection breakingNews = epg.getSections().get(0);

    assertEquals(25, breakingNews.getLinkItems().size());
    assertSection(breakingNews);

    EPGIndex index = epg.getEgpIndex();
    assertNotNull(index);
    assertTrue(index.getSections().size() > 0);

    breakingNews = index.getSections().get(0);
    assertEquals("BREAKING NEWS", breakingNews.getName());
    assertEquals(3, breakingNews.getSections().size());
  }

  private void assertSection(EPGSection section) {
    boolean atLeastOneVideo = false;
    for (EPGLinkItem linkItem : section.getLinkItems()) {
      assertNotNull("Null link item thumbnail for " + linkItem.getId(), linkItem.getThumbnail());

      for (EPGContent content : linkItem.getContents()) {
        if (content.isVideo()) {
          atLeastOneVideo = true;
        }
        assertNotNull(
            "Null link item content thumbnail for " + linkItem.getId() + ":" + content.getName(),
            content.getThumbnail());
      }
    }
    assertTrue("Expecting at least one video", atLeastOneVideo);
  }

}
