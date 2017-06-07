package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeAdapter extends ArrayAdapter {

	private ArrayList<Earthquake> mEarthquakesList;


	public EarthquakeAdapter(Context context, ArrayList<Earthquake> earthquakes) {
		super(context, 0, earthquakes);

		// Initialize list
		mEarthquakesList = earthquakes;

	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		// Inflate complex view (3 TextViews) for presenting the quake report details
		if (convertView == null) {
			// No views available to recycle
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list, parent, false);
		}

		// Get earthquake report details from list
		Earthquake currentItem = mEarthquakesList.get(position);

		// Map TextView to earthquake's magnitude
		TextView magTextView = (TextView) convertView.findViewById(R.id.magnitude_text_view);
		magTextView.setText(String.valueOf(currentItem.getMag()));

		// Map TextView to earthquake's location
		TextView locationTextView = (TextView) convertView.findViewById(R.id.location_text_view);
		locationTextView.setText(currentItem.getPlace());

		// // Map TextView to earthquake's date
		TextView dateTextView = (TextView) convertView.findViewById(R.id.date_text_view);
		dateTextView.setText(String.valueOf(currentItem.getDate()));

		return convertView;
	}
}
