package com.jeffpalm.android.util.urls;

import java.io.IOException;
import java.io.InputStream;

public class StringInputStream extends InputStream {

	private final String string;
	private int currentIndex;

	public StringInputStream(String string) {
		this.string = string;
	}

	@Override
	public int read() throws IOException {
		return currentIndex >= string.length() ? -1 : string.charAt(currentIndex++);
	}

	/** @return the underlying string */
	public String getString() {
		return string;
	}

}
