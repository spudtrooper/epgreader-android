package com.jeffpalm.android.tmz.model;

import java.util.Collections;
import java.util.List;

/**
 * A class to access a {@code TMZIndex}.
 */
public final class TMZWrapper {

	private final TMZ tmz;
	private final TMZIndexWrapper index;

	public TMZWrapper(TMZ tmz) {
		this.tmz = tmz;
		this.index = new TMZIndexWrapper(tmz.getIndex());
	}

	/** @return the link items from the main section */
	public List<? extends TMZItem<?>> getLinkItems() {
		return getSection().getLinkItems();
	}

	/** @return the underlying index */
	public TMZIndex getIndex() {
		return tmz.getIndex();
	}

	/** @return the subsections for the main section */
	public List<TMZSection> getSubSections() {
		TMZSection section = index.getSection(getSection().getId());
		return section != null ? section.getSections() : Collections.<TMZSection> emptyList();
	}

	private TMZSection getSection() {
		return tmz.getSection();
	}
}
