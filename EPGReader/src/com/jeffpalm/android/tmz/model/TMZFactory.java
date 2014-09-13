package com.jeffpalm.android.tmz.model;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.xmlpull.v1.XmlPullParserException;

import com.jeffpalm.android.epg.EPG;
import com.jeffpalm.android.epg.EPGParser;

import android.content.Context;

public class TMZFactory {

  public TMZ read(Context context, String url) throws IllegalStateException,
      XmlPullParserException, IOException {
    EPGParser parser = new EPGParser();
    EPG epg = parser.parse(openStream(url, 10000));
    TMZ tmz = new TMZ(epg);
    return tmz;
  }

  /** Opens the input stream for the url. */
  private static InputStream openStream(String url, int connectionTimeout)
      throws IllegalStateException, IOException {
    HttpGet httpGet = new HttpGet(url);
    HttpParams httpParameters = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
    HttpClient httpClient = new DefaultHttpClient(httpParameters);
    HttpResponse response = httpClient.execute(httpGet, new BasicHttpContext());
    if (response.getStatusLine().getStatusCode() == 200) {
      return response.getEntity().getContent();
    }
    return null;
  }
}
