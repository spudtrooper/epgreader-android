package com.jeffpalm.android.epg;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.SystemClock;
import android.util.Log;
import android.util.Xml;

import com.jeffpalm.android.epg.EPGSection.Builder;
import com.jeffpalm.android.util.Util;

public final class EPGParser {

  private final static String TAG = "Parser";

  private final static String ns = null;

  /** Thrown to escape and return when an error has occured. */
  private static final class DoneException extends RuntimeException {
  }

  private static final class Result<T> {
    final T value;
    final boolean done;

    Result(T value, boolean done) {
      this.value = value;
      this.done = done;
    }

    public static <T> Result<T> create(T value, boolean done) {
      return new Result<T>(value, done);
    }
  }

  public EPG parse(InputStream in) throws IOException, XmlPullParserException {
    EPG.Builder builder = EPG.builder();
    try {
      long start = SystemClock.currentThreadTimeMillis();
      XmlPullParser parser = Xml.newPullParser();
      parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
      parser.setInput(new StringReader(Util.getParsingSafeString(in)));
      parser.nextTag();
      long createdParserStop = SystemClock.currentThreadTimeMillis();
      Log.d(TAG, "Created parser in " + (createdParserStop - start) + "ms");
      readFeed(parser, builder);
      long readParserStop = SystemClock.currentThreadTimeMillis();
      Log.d(TAG, "Read parser in " + (readParserStop - createdParserStop) + "ms");
    } catch (DoneException e) {
      Log.d(TAG, "Caught done exception, returning early.");
    } finally {
      in.close();
    }
    return builder.build();
  }

  private void readFeed(XmlPullParser parser, EPG.Builder builder) throws XmlPullParserException,
      IOException {
    Log.v(TAG, "readFeed");
    parser.require(XmlPullParser.START_TAG, ns, "epg");
    while (next(parser)) {
      if (parser.getEventType() == XmlPullParser.END_DOCUMENT
          || (parser.getEventType() == XmlPullParser.END_TAG && "epg".equals(parser.getName()))) {
        break;
      }
      if (parser.getEventType() == XmlPullParser.START_TAG && "section".equals(parser.getName())) {
        Result<EPGSection> sectionResult = readSection(parser);
        builder.withSection(sectionResult.value);
        if (sectionResult.done) {
          break;
        }
      }
      if (parser.getEventType() == XmlPullParser.START_TAG && "epg:index".equals(parser.getName())) {
        Result<EPGIndex> epgIndexResult = readEpgIndex(parser);
        builder.withEpgIndex(epgIndexResult.value);
        if (epgIndexResult.done) {
          break;
        }
      }
    }
    // Ensure there is an index. If an error ocurred, we may have returned early.
    if (!builder.hasEpgIndex()) {
      builder.withEpgIndex(EPGIndex.builder().build());
    }
  }

  private Result<EPGIndex> readEpgIndex(XmlPullParser parser) throws XmlPullParserException,
      IOException {
    Log.v(TAG, "EgpIndex");
    EPGIndex.Builder builder = EPGIndex.builder();
    boolean done = false;
    while (next(parser)) {
      if (parser.getEventType() == XmlPullParser.END_TAG && "epg:index".equals(parser.getName())) {
        break;
      }
      if (parser.getEventType() == XmlPullParser.START_TAG && "section".equals(parser.getName())) {
        Result<EPGSection> sectionResult = readSection(parser);
        builder.withSection(sectionResult.value);
        if (sectionResult.done) {
          done = true;
          break;
        }
      }
    }
    return Result.create(builder.build(), done);
  }

  private Result<EPGSection> readSection(XmlPullParser parser) throws XmlPullParserException,
      IOException {
    Log.v(TAG, "readSection");
    EPGSection.Builder builder = EPGSection.builder();
    readSectionAttributes(parser, builder);
    boolean done = false;
    while (next(parser)) {
      if (parser.getEventType() == XmlPullParser.END_DOCUMENT
          || (parser.getEventType() == XmlPullParser.END_TAG && "section".equals(parser.getName()))) {
        break;
      }
      if (parser.getEventType() == XmlPullParser.START_TAG && "section".equals(parser.getName())) {
        Result<EPGSection> sectionResult = readSection(parser);
        builder.withSection(sectionResult.value);
      }
      if (parser.getEventType() == XmlPullParser.START_TAG && "link_item".equals(parser.getName())) {
        try {
          Result<EPGLinkItem> linkItemResult = readLinkItem(parser);
          builder.withLinkItem(linkItemResult.value);
          if (linkItemResult.done) {
            done = true;
            break;
          }
        } catch (Exception e) {
          Log.e(TAG, e.getMessage());
          // throw new DoneException();
          break;
        }
      }
    }
    return Result.create(builder.build(), done);
  }

  private boolean next(XmlPullParser parser) {
    try {
      parser.next();
      return true;
    } catch (Exception e) {
      Log.e(TAG, e.getMessage());
    }
    return false;
  }

  private void readSectionAttributes(XmlPullParser parser, Builder builder) {
    String id = getAttributeValue("section", parser, "id");
    builder.withId(id);
    String name = getAttributeValue("section", parser, "name");
    builder.withName(name);
    String href = getAttributeValue("section", parser, "href");
    builder.withHref(href);
    String pagelength = getAttributeValue("section", parser, "pagelength");
    builder.withPageLength(pagelength);
  }

  /**
   * @param parser
   * @param tagName
   * @return whether there was an error
   * @throws XmlPullParserException
   * @throws IOException
   */
  private boolean readUntilEndTag(XmlPullParser parser, String tagName)
      throws XmlPullParserException, IOException {
    boolean done = false;
    while (true) {
      // This could throw an Exception, if it does notify the caller.
      try {
        parser.next();
      } catch (Exception e) {
        done = true;
        break;
      }
      int eventType = parser.getEventType();
      if (eventType == XmlPullParser.END_DOCUMENT
          || (eventType == XmlPullParser.END_TAG && tagName.equals(parser.getName()))) {
        break;
      }
    }
    return done;
  }

  private String getAttributeValue(String tagName, XmlPullParser parser, String attributeName) {
    Log.v(TAG, "getAttributeValue for " + tagName + "." + attributeName);
    String result = parser.getAttributeValue(ns, attributeName);
    Log.v(TAG, tagName + "." + attributeName + " = " + result);
    return result;
  }

  private Result<EPGLinkItem> readLinkItem(XmlPullParser parser) throws XmlPullParserException,
      IOException {
    Log.v(TAG, "readLinkItem");

    EPGLinkItem.Builder builder = EPGLinkItem.builder();
    parser.require(XmlPullParser.START_TAG, ns, "link_item");

    String id = getAttributeValue("link_item", parser, "id");
    builder.withId(id);

    String category = getAttributeValue("link_item", parser, "category");
    builder.withCategory(category);

    String name = getAttributeValue("link_item", parser, "name");
    builder.withName(name);

    String shareUrl = getAttributeValue("link_item", parser, "share_url");
    builder.withLink(shareUrl);

    String url = getAttributeValue("link_item", parser, "url");
    builder.withUrl(url);

    int tag = parser.nextTag();

    if (tag == XmlPullParser.START_TAG && "description".equals(parser.getName())) {
      String description = readTextNode("link_item", parser, "description");
      builder.withDescription(description);
      parser.nextTag();
    }

    if (tag == XmlPullParser.START_TAG && "thumbnail".equals(parser.getName())) {
      String thumbnail = readThumbnail(parser);
      builder.withThumbnail(thumbnail);
      parser.nextTag();
    }

    readContents(parser, builder);

    boolean done = readUntilEndTag(parser, "link_item");

    return Result.create(builder.build(), done);
  }

  private void readContents(XmlPullParser parser, EPGLinkItem.Builder builder)
      throws XmlPullParserException, IOException {
    Log.v(TAG, "readContents");

    while (next(parser)) {
      if (parser.getEventType() == XmlPullParser.END_TAG && "link_item".equals(parser.getName())) {
        break;
      }
      if (parser.getEventType() == XmlPullParser.START_TAG && "content".equals(parser.getName())) {
        builder.withContent(readContent(parser));
      }
    }
  }

  private EPGContent readContent(XmlPullParser parser) throws XmlPullParserException, IOException {
    Log.v(TAG, "readContent");

    EPGContent.Builder builder = EPGContent.builder();
    parser.require(XmlPullParser.START_TAG, ns, "content");

    String id = getAttributeValue("content", parser, "id");
    builder.withId(id);

    String mediaType = getAttributeValue("content", parser, "mediaType");
    builder.withMediaType(mediaType);

    String category = getAttributeValue("content", parser, "category");
    builder.withCategory(category);

    String name = getAttributeValue("content", parser, "name");
    builder.withName(name);

    String shareUrl = getAttributeValue("content", parser, "share_url");
    builder.withLink(shareUrl);

    String url = getAttributeValue("content", parser, "url");
    builder.withUrl(url);

    int tag = parser.nextTag();

    if (tag == XmlPullParser.START_TAG && "thumbnail".equals(parser.getName())) {
      String thumbnail = readThumbnail(parser);
      builder.withThumbnail(thumbnail);
    }

    readUntilEndTag(parser, "content");

    return builder.build();
  }

  private String readThumbnail(XmlPullParser parser) throws XmlPullParserException, IOException {
    String src = parser.getAttributeValue(null, "src");
    return src;
  }

  private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
    StringBuilder sb = new StringBuilder();
    if (parser.next() == XmlPullParser.TEXT) {
      sb.append(parser.getText());
    }
    return sb.toString();
  }

  private String readTextNode(String tagName, XmlPullParser parser, String nodeName)
      throws XmlPullParserException, IOException {
    Log.v(TAG, "readTextNode " + tagName + "." + nodeName);
    parser.require(XmlPullParser.START_TAG, ns, nodeName);
    String text = readText(parser);
    Log.v(TAG, tagName + "." + nodeName + " = " + text);
    parser.nextTag();
    return text;
  }
}
