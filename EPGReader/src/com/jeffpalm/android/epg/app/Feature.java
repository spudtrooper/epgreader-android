package com.jeffpalm.android.epg.app;

public enum Feature {

	/** Whether to enable videos. */
	Videos(false),
	
	/** Whether to enable photos. */
	Photos(true),
	
	/** Whether to enable TV. */
	TV(false);

	private boolean enabled;

	private Feature(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
