package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

class EarthquakeLoader extends AsyncTaskLoader {

	/**
	 * Tag for log messages
	 */
	private static final String LOG_TAG = EarthquakeLoader.class.getSimpleName();

	/**
	 * Query url
	 */
	private String mUrl;

	EarthquakeLoader(Context context, String url) {
		super(context);
		mUrl = url;
	}

	@Override
	protected void onStartLoading() {
		forceLoad();
	}

	@Override
	public Object loadInBackground() {
		if (mUrl == null) {
			return null;
		}
		// Perform the network request, parse the response, and extract a list of earthquakes.
		return QueryUtils.fetchEarthquakeData(mUrl);
	}
}
