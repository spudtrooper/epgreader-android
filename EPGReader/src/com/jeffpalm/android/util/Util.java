package com.jeffpalm.android.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;

import com.jeffpalm.android.util.urls.StringInputStream;

public final class Util {

  private Util() {
  }

  /**
   * @param input the input stream from which to read
   * @return a safe string to parse -- e.g. replacing all ampersands
   * @throws IOException
   */
  public static String getParsingSafeString(InputStream input) throws IOException {
    if (input instanceof StringInputStream) {
      StringInputStream stringInput = (StringInputStream) input;
      return stringInput.getString();
    }
    StringBuilder sb = new StringBuilder();
    BufferedReader in = new BufferedReader(new InputStreamReader(input));
    String line;
    while ((line = in.readLine()) != null) {
      sb.append(getParsingSafeString(line));
      // sb.append("\n");
    }
    return sb.toString();
  }

  /**
   * Gets a string safe for parsing with unknown entity refs replaced with ascii ones.
   * 
   * @param line string to sanitize
   * @return a string safe for parsing with unknown entity refs replaced with ascii ones.
   */
  public static String getParsingSafeString(String line) {
    line = line.replaceAll("&(?!\\w+;)", "&amp;");

    return line;
  }

  /**
   * Opens the input stream for the url.
   */
  public static InputStream openStream(String url, Integer connectionTimeout)
      throws IllegalStateException, IOException {
    HttpGet httpGet = new HttpGet(url);
    HttpParams httpParameters = new BasicHttpParams();
    if (connectionTimeout != null) {
      HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
    }
    HttpClient httpClient = new DefaultHttpClient(httpParameters);
    HttpResponse response = httpClient.execute(httpGet, new BasicHttpContext());
    if (response.getStatusLine().getStatusCode() == 200) {
      return response.getEntity().getContent();
    }
    return null;
  }
}
