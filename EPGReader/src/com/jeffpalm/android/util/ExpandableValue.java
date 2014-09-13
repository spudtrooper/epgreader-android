package com.jeffpalm.android.util;


/**
 * A value with a settable expanded value.
 * 
 * @type T The type of the underlying value.
 */
public final class ExpandableValue<T> {
	
	private final T value;
	private boolean isExpanded = false;
	
	public static <T> ExpandableValue<T> create(T value) {
		return new ExpandableValue<T>(value);
	}

	public ExpandableValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void toggle() {
		isExpanded = !isExpanded;
	}
}
