package com.example.android.quakereport;

public class Earthquake {
	private double mMag;
	private String mPlace;
	private long mTime;

	public Earthquake(double mMag, String mPlace, long mTime) {
		this.mMag = mMag;
		this.mPlace = mPlace;
		this.mTime = mTime;
	}

	public double getMag() {
		return mMag;
	}

	public String getPlace() {
		return mPlace;
	}

	public long getDate() {
		return mTime;
	}
}
