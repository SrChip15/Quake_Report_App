/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class EarthquakeActivity
		extends AppCompatActivity
		implements LoaderCallbacks<List<Earthquake>> {

	private static final String LOG_TAG = EarthquakeActivity.class.getName();

	/**
	 * Sample JSON response for a USGS query
	 */
	private static final String USGS_REQUEST_URL =
			"https://earthquake.usgs.gov/fdsnws/event/1/query";

	/**
	 * Constant value for the earthquake loader ID. We can choose any integer.
	 * This really only comes into play if you're using multiple loaders.
	 */
	private static final int EARTHQUAKE_LOADER_ID = 1;

	/**
	 * Adapter for the list of earthquakes
	 */
	private EarthquakeAdapter mAdapter;

	/**
	 * TextView for empty state
	 */
	private TextView mEmptyStateView;

	private View mProgressSpinner;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.earthquake_activity);

		// Find a reference to the {@link ListView} in the layout
		ListView earthquakeListView = (ListView) findViewById(R.id.list);

		// Create a new adapter that takes an empty list of earthquakes as input
		mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

		// Set the adapter on the {@link ListView}
		// so the list can be populated in the user interface
		assert earthquakeListView != null;
		earthquakeListView.setAdapter(mAdapter);

		// empty state
		mEmptyStateView = (TextView) findViewById(R.id.empty_state_text_view);
		earthquakeListView.setEmptyView(mEmptyStateView);

		// get a reference to the progress bar
		mProgressSpinner = findViewById(R.id.loading_spinner);

		// Get a reference to the ConnectivityManager to check state of network connectivity
		ConnectivityManager cm =
				(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		// Get details on the currently active default data network
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		// Set boolean for whether the host system is connected to the internet or not
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

		// Set an item click listener on the ListView, which sends an intent to a web browser
		// to open a website with more information about the selected earthquake.
		earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				// Find the current earthquake that was clicked on
				Earthquake currentEarthquake = (Earthquake) mAdapter.getItem(position);

				// Convert the String URL into a URI object (to pass into the Intent constructor)
				assert currentEarthquake != null;
				Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

				// Create a new intent to view the earthquake URI
				Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

				// Send the intent to launch a new activity
				startActivity(websiteIntent);
			}
		});
		if (isConnected) {
			// Get a reference to the LoaderManager, in order to interact with loaders.
			LoaderManager loaderManager = getLoaderManager();

			// Initialize the loader. Pass in the int ID constant defined above and pass in null for
			// the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
			// because this activity implements the LoaderCallbacks interface).
			loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
		} else {
			// Otherwise, display error
			// First, hide loading indicator so error message will be visible
			mProgressSpinner.setVisibility(View.GONE);

			// Update empty state with no connection error message
			mEmptyStateView.setText(R.string.no_internet);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Get the id of the item that was clicked
		int id = item.getItemId();

		// Check if menu option was clicked by the user
		if (id == R.id.action_settings) {
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		}

		// Default behaviour
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String minMagnitude = sharedPrefs.
				getString(
						getString(R.string.settings_min_magnitude_key),
						getString(R.string.settings_min_magnitude_default)
				);

		String orderBy = sharedPrefs
				.getString(
						getString(R.string.settings_order_by_key),
						getString(R.string.settings_order_by_default)
				);

		Uri baseUrl = Uri.parse(USGS_REQUEST_URL);
		Uri.Builder uriBuilder = baseUrl.buildUpon();

		uriBuilder.appendQueryParameter("format", "geojson");
		uriBuilder.appendQueryParameter("limit", "20");
		uriBuilder.appendQueryParameter("minmag", minMagnitude);
		uriBuilder.appendQueryParameter("orderby", orderBy);

		return new EarthquakeLoader(this, uriBuilder.toString());
	}

	@Override
	public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
		// Hide progress bar
		assert mProgressSpinner != null;
		mProgressSpinner.setVisibility(View.GONE);

		// Set empty state {@link TextView}
		mEmptyStateView.setText(R.string.empty_state_text);

		// Clear the adapter of previous earthquake data
		mAdapter.clear();

		// If there is a valid list of {@link Earthquake}s, then add them to the adapter's
		// data set. This will trigger the ListView to update.
		if (earthquakes != null && !earthquakes.isEmpty()) {
			mAdapter.addAll(earthquakes);
		}
	}

	@Override
	public void onLoaderReset(Loader loader) {
		// Log message
		Log.v(LOG_TAG, "loaderReset()");
		// Loader reset, so we can clear out our existing data.
		mAdapter.clear();
	}
}
