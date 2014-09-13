package com.jeffpalm.android.epg.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.Uri;
import android.util.Log;

/**
 * A class to find the MP4 video URL given a video page URL.
 */
public final class EPGReaderUrlFinder {

  private static final String TAG = "TMZUrlFinder";

  // "videoUrl":
  // "http:\/\/tmz.vo.llnwd.net\/o28\/2014-05\/04\/0_35wig9pj_0_3yuavu7l_2.mp4",
  private static final Pattern VIDEO_URL_PATTERN = Pattern.compile("\"videoUrl\":\\s+\"([^\"]+)\"");

  // class="lightbox-link" href="http://www.tmz.com/videos/0_vcbn115m"
  private static final Pattern VIDEO_PAGE_URL_PATTERN = Pattern
      .compile("class=\"lightbox-link\"\\s+href=\"([^\"]+)\"");

  // <img alt="0828-joan-rivers-tmz-05"
  // src="http://ll-media.tmz.com/2014/08/30/0828-joan-rivers-tmz-6.jpg">
  private static final Pattern WEB_PAGE_IMAGE_URL_PATTERN = Pattern
      .compile("<img alt=\"[^\"]+\"\\s*src=\"(http:\\/\\/ll-media.tmz.com\\/[^\"]+)\"");

  /**
   * 
   * @param webPageUrl url of the mobile web page story
   * @return the main photo src URL or null if not found
   * @throws IOException
   */
  public Uri getPhotoUrl(String webPageUrl) throws IOException {
    Log.d(TAG, "getPhotoUrl url=" + webPageUrl);
    return getPageUri(webPageUrl, WEB_PAGE_IMAGE_URL_PATTERN);
  }

  /**
   * @param storyUrl The main story url -- e.g. http://www.tmz.com/2014/05/04/obama-president
   *          -wash-car-white-house-correspondents-dinner-secret-service/
   * @return
   * @throws IOException MP4 url for the main story url
   */
  public Uri getStoryUrl(String storyUrl) throws IOException {
    Log.d(TAG, "Finding video page for " + storyUrl);
    String videoPageUrl = getVideoPageUrl(storyUrl);
    Log.d(TAG, "Found video page " + videoPageUrl);
    if (videoPageUrl == null) {
      return null;
    }
    return getVideoUri(videoPageUrl);
  }

  private String getVideoPageUrl(String storyUrl) throws IOException {
    return getPageUrl(storyUrl, VIDEO_PAGE_URL_PATTERN);
  }

  private String getPageUrl(String link, Pattern p) throws IOException {
    Uri uri = getPageUri(link, p);
    return uri != null ? uri.toString() : null;
  }

  private Uri getPageUri(String link, Pattern p) throws IOException {
    URL url = new URL(link);
    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
    try {
      String line;
      while ((line = in.readLine()) != null) {
        Matcher m = p.matcher(line);
        if (m.find()) {
          String resultString = m.group(1);
          try {
            return Uri.parse(resultString);
          } catch (Exception ignored) {
          }
        }
      }
    } finally {
      in.close();
    }
    return null;
  }

  /**
   * @param storyUrl The video url -- e.g. www.tmz.com/videos/0_35wig9pj/
   * @return
   * @throws IOException MP4 url for the video story url
   */
  public Uri getVideoUri(String videoPageUrl) throws IOException {
    Log.d(TAG, "Finding video URI for " + videoPageUrl);
    Uri uri = getVideoUriInternal(videoPageUrl);
    Log.d(TAG, "Found video URI " + uri);
    return uri;
  }

  private Uri getVideoUriInternal(String videoPageUrl) throws IOException {
    URL url = new URL(videoPageUrl);
    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
    try {
      Pattern p = VIDEO_URL_PATTERN;
      String line;
      while ((line = in.readLine()) != null) {
        Matcher m = p.matcher(line);
        if (m.find()) {
          String videoUrl = m.group(1);
          videoUrl = videoUrl.replaceAll("\\\\", "");
          try {
            return Uri.parse(videoUrl);
          } catch (Exception e) {
          }
        }
      }
    } finally {
      in.close();
    }
    return null;
  }
}
