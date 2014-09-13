package com.jeffpalm.android.util.urls;

import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream that delegates to another input stream and writes the result
 * on close to the database.
 */
final class WriteToDatabaseOnCloseInputStream extends InputStream {

	private final String url;
	private final InputStream inputStream;
	private final DatabaseHandler dbHandler;

	private final StringBuilder buffer = new StringBuilder();

	WriteToDatabaseOnCloseInputStream(String url, InputStream inputStream, DatabaseHandler dbHandler) {
		this.url = url;
		this.inputStream = inputStream;
		this.dbHandler = dbHandler;
	}

	@Override
	public int read() throws IOException {
		int c = inputStream.read();
		buffer.append((char) c);
		return c;
	}

	@Override
	public void close() throws IOException {
		super.close();
		inputStream.close();
		dbHandler.insertUrl(url, buffer.toString());
	}
}
