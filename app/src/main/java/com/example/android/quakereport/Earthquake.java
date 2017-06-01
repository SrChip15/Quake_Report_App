package com.example.android.quakereport;

public class Earthquake {
	private float mMag;
	private String mPlace;
	private String mDate;

	public Earthquake(float mMag, String mPlace, String mDate) {
		this.mMag = mMag;
		this.mPlace = mPlace;
		this.mDate = mDate;
	}

	public float getMag() {
		return mMag;
	}

	public String getPlace() {
		return mPlace;
	}

	public String getDate() {
		return mDate;
	}
}
