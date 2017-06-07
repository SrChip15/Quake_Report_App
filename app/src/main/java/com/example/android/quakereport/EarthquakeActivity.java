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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private ArrayList<Earthquake> mEarthquakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of mEarthquakes locations.
        mEarthquakes = QueryUtils.extractEarthquakes();

        // no parse data for simple view
        /*mEarthquakes.add(new Earthquake(5.5f, "San Francisco", "Feb 2, 2016"));
        mEarthquakes.add(new Earthquake(3.5f, "London", "Feb 2, 2016"));
        mEarthquakes.add(new Earthquake(4.5f, "Tokyo", "Feb 2, 2016"));
        mEarthquakes.add(new Earthquake(2.5f, "India", "Feb 2, 2016"));
        mEarthquakes.add(new Earthquake(6.5f, "China", "Feb 2, 2016"));
        mEarthquakes.add(new Earthquake(9.0f, "Indonesia", "Feb 2, 2016"));
        mEarthquakes.add(new Earthquake(4.75f, "Mexico", "Feb 2, 2016"));*/



        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of mEarthquakes
        EarthquakeAdapter adapter = new EarthquakeAdapter(EarthquakeActivity.this, mEarthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

    }
}
