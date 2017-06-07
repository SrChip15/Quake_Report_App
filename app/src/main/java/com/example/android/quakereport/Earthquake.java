package com.example.android.quakereport;

public class Earthquake {
	private double mMagnitude;
	private String mPlace;
	private long mDateAndTime;
	private String mUrl;

	public Earthquake(double mag, String place, long dateAndTime, String url) {
		this.mMagnitude = mag;
		this.mPlace = place;
		this.mDateAndTime = dateAndTime;
		this.mUrl = url;
	}

	double getMag() {
		return mMagnitude;
	}

	String getPlace() {
		return mPlace;
	}

	long getDateAndTime() {
		return mDateAndTime;
	}

	public String getUrl() {
		return mUrl;
	}
}
