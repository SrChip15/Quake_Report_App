package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
final class QueryUtils {

	private static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();

	/**
	 * Create a private constructor because no one should ever create a {@link QueryUtils} object.
	 * This class is only meant to hold static variables and methods, which can be accessed
	 * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
	 */
	private QueryUtils() {

	}

	/**
	 * Query the USGS dataset and return a list of {@link Earthquake} objects.
	 */
	static List<Earthquake> fetchEarthquakeData(String requestUrl) {
		// Create URL object
		URL url = createUrl(requestUrl);

		// Perform HTTP request to the URL and receive a JSON response back
		String jsonResponse = null;
		try {
			jsonResponse = makeHttpRequest(url);
		} catch (IOException e) {
			Log.e(LOG_TAG, "Problem making the HTTP request.", e);
		}

		// Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
		// Return the list of {@link Earthquake}s
		return extractFeatureFromJson(jsonResponse);
	}

	private static URL createUrl(String stringUrl) {
		URL url;
		try {
			url = new URL(stringUrl);
		} catch (MalformedURLException exception) {
			Log.e(LOG_TAG, "Problem building the URL ", exception);
			return null;
		}
		return url;
	}

	private static String makeHttpRequest(URL url) throws IOException {
		String jsonResponse = "";

		// If the URL is null, then return early
		if (url == null) {
			return jsonResponse;
		}

		HttpURLConnection urlConnection = null;
		InputStream inputStream = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setReadTimeout(10_000 /* milliseconds */);
			urlConnection.setConnectTimeout(15_000 /* milliseconds */);
			urlConnection.connect();
			// Check for successful connection response code
			if (urlConnection.getResponseCode() == 200) {
				// Connection established
				inputStream = urlConnection.getInputStream();
				jsonResponse = readFromStream(inputStream);
			} else {
				// Connection failure
				// Log error in connection
				Log.e(LOG_TAG, "Error Code: " + urlConnection.getResponseCode());
			}
		} catch (IOException e) {
			Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results. ", e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (inputStream != null) {
				// Closing the input stream could throw an IOException, which is why
				// the makeHttpRequest(URL url) method signature specifies than an IOException
				// could be thrown.
				inputStream.close();
			}
		}
		return jsonResponse;
	}

	/**
	 * Convert the {@link InputStream} into a String which contains the
	 * whole JSON response from the server.
	 */
	private static String readFromStream(InputStream inputStream) throws IOException {
		StringBuilder output = new StringBuilder();
		if (inputStream != null) {
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream,
					Charset.forName("UTF-8")); // decoding the input bits
			BufferedReader reader = new BufferedReader(inputStreamReader);
			String line = reader.readLine();
			while (line != null) { // until all data has been transmitted from the http request
				output.append(line);
				line = reader.readLine(); // parse next line of decoded input stream
			}
		}
		return output.toString(); // return string from string builder object
	}


	/**
	 * Return a list of {@link Earthquake} objects that has been built up from
	 * parsing a JSON response.
	 */
	private static List<Earthquake> extractFeatureFromJson(String jsonResponse) {
		// If the JSON string is empty or null, then return early.
		if (TextUtils.isEmpty(jsonResponse)) {
			return null;
		}

		// Create an empty ArrayList that we can start adding earthquakes to
		List<Earthquake> earthquakes = new ArrayList<>();

		// Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
		// is formatted, a JSONException exception object will be thrown.
		// Catch the exception so the app doesn't crash, and print the error message to the logs.
		try {
			// Parse the json string
			JSONObject response = new JSONObject(jsonResponse);

			// Get target array
			JSONArray earthquakeArray = response.getJSONArray("features");

			// Traverse through each earthquake json object
			if (earthquakeArray.length() > 0) {
				for (int i = 0; i < earthquakeArray.length(); i++) {
					// Get earthquake at position
					JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

					// Grab properties to extract data
					JSONObject properties = currentEarthquake.getJSONObject("properties");

					// Get required data
					// Get magnitude of earthquake
					double magnitude = properties.getDouble("mag");

					// Get location details of earthquake
					String place = properties.getString("place");

					// Get the time at which the earthquake occurred
					long rawTime = properties.getLong("time");

					// Get the website url for the earthquake
					String url = properties.getString("url");

					// Add data to list
					earthquakes.add(new Earthquake(magnitude, place, rawTime, url));
				}
				// Return the list of earthquakes
				return earthquakes;
			}

		} catch (JSONException e) {
			// If an error is thrown when executing any of the above statements in the "try" block,
			// catch the exception here, so the app doesn't crash. Print a log message
			// with the message from the exception.
			Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
		}

		// return nothing if there is problem with JSON extraction
		return null;
	}
}