package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

class EarthquakeAdapter extends ArrayAdapter {

	private ArrayList<Earthquake> mEarthquakesList;


	EarthquakeAdapter(Context context, ArrayList<Earthquake> earthquakes) {
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
			convertView = LayoutInflater.from(getContext())
					.inflate(R.layout.earthquake_list, parent, false);
		}

		// Get earthquake report details from list
		Earthquake currentQuake = mEarthquakesList.get(position);

		// Get the magnitude value
		double magnitude = currentQuake.getMag();

		// Format magnitude value to display one point precision
		DecimalFormat magnitudeFormatter = new DecimalFormat("0.0");
		String formattedMagnitude = magnitudeFormatter.format(magnitude);

		// Map TextView to earthquake's magnitude
		TextView magTextView = (TextView) convertView.findViewById(R.id.magnitude_text_view);
		magTextView.setText(formattedMagnitude);

		// Set the proper background color on the magnitude circle.
		// Fetch the background from the TextView, which is a GradientDrawable.
		GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

		// Get the appropriate background color based on the current earthquake magnitude
		int magnitudeColor = getMagnitudeColor(currentQuake.getMag());

		// Set the color on the magnitude circle
		magnitudeCircle.setColor(magnitudeColor);

		// Map location information
		// Get full location information from earthquake
		String fullLocation = currentQuake.getPlace();

		// Split fullLocation into primary and secondary location information
		String primaryLocation = "";
		String secondaryLocation = "";
		if (fullLocation.contains("of")) {
			int splitAtIndex = fullLocation.indexOf("f") + 1;
			primaryLocation = fullLocation.substring(splitAtIndex, fullLocation.length()).trim();
			secondaryLocation = fullLocation.substring(0, splitAtIndex).trim();
		} else {
			primaryLocation = fullLocation;
			secondaryLocation = getContext().getString(R.string.near_the);
		}

		// Hook {@link TextView} to primary location information
		TextView primaryLocationTextView = (TextView) convertView
				.findViewById(R.id.location_primary_text_view);
		primaryLocationTextView.setText(primaryLocation);

		// Map {@link TextView} to earthquake's location
		TextView secondaryLocationTextView = (TextView) convertView
				.findViewById(R.id.location_secondary_text_view);
		secondaryLocationTextView.setText(secondaryLocation);

		// Map {@link TextView} to earthquake's date
		// Format unix timestamp to human readable date format
		// Get date and time information from unix timestamp
		Date getDateAndTime = new Date(currentQuake.getDateAndTime());
		// Initialize {@link SimpleDateFormat) with locale date format
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy", Locale.US);
		// Format date using the formatter
		String formattedDate = dateFormatter.format(getDateAndTime);
		// Set the formatted date as text to display
		TextView dateTextView = (TextView) convertView.findViewById(R.id.date_text_view);
		dateTextView.setText(formattedDate);

		// Map {@link TextView} to earthquake's time
		// Initialize {@link SimpleDateFormat) with locale time format
		SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a", Locale.US);
		// Format time information using the time formatter
		String formattedTime = timeFormatter.format(getDateAndTime);
		// Set the formatted date as text to display
		TextView timeTextView = (TextView) convertView.findViewById(R.id.time_text_view);
		timeTextView.setText(formattedTime);

		return convertView;
	}

	private int getMagnitudeColor(double magnitude) {
		int magAsInteger = (int) Math.floor(magnitude);
		int magnitudeColorResourceId = 0;
		switch (magAsInteger) {
			case 0:
			case 1:
				magnitudeColorResourceId = R.color.magnitude1;
				break;
			case 2:
				magnitudeColorResourceId = R.color.magnitude2;
				break;
			case 3:
				magnitudeColorResourceId = R.color.magnitude3;
				break;
			case 4:
				magnitudeColorResourceId = R.color.magnitude4;
				break;
			case 5:
				magnitudeColorResourceId = R.color.magnitude5;
				break;
			case 6:
				magnitudeColorResourceId = R.color.magnitude6;
				break;
			case 7:
				magnitudeColorResourceId = R.color.magnitude7;
				break;
			case 8:
				magnitudeColorResourceId = R.color.magnitude8;
				break;
			case 9:
				magnitudeColorResourceId = R.color.magnitude9;
				break;
			default:
				magnitudeColorResourceId = R.color.magnitude10plus;
				break;
		}
		return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
	}
}
