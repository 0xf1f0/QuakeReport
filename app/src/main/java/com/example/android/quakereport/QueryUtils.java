package com.example.android.quakereport;

/**
 * Created by Lenovo on 6/27/2017.
 */

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
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils
{
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils()
    {

    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl)
    {
        URL url = null;
        try
        {
            url = new URL(stringUrl);
        } catch (MalformedURLException e)
        {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


    /**
     * Query the USGS dataset and return an {@link ArrayList<Earthquake>} object to represent a
     * single
     * earthquake.
     */
    public static List<Earthquake> fetchEarthquakeData(String requestUrl)
    {
        //Wait 2 sec before earthquake data is fetched
        //Show the progress bar during this period
//        try
//        {
//            Thread.sleep(2000);
//        } catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }

        Log.i(LOG_TAG, "-> Calling: fetchEarthquakeData");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try
        {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e)
        {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an
        // {@link ArrayList<Earthquake> } object


        // Return the {@link ArrayList<Earthquake>}
        return extractFeatureFromJSON(jsonResponse);
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractFeatureFromJSON(String earthquakeJSON)
    {
        // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
        // build up a list of Earthquake objects with the corresponding data.

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON))
        {
            return null;
        }

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try
        {
            JSONObject jsonObject = new JSONObject(earthquakeJSON);

            //Get the JSON Array Node "features"
            JSONArray features = jsonObject.getJSONArray("features");

            // If there are results in the features array
            if (features.length() > 0)
            {
                //Loop through each index of the JSONArray to extract the mag, place, and time
                for (int i = 0; i < features.length(); i++)
                {
                    //Create a JSONObject for each index of features
                    JSONObject index = features.getJSONObject(i);

                    //Create a JSONObject for properties
                    JSONObject properties = index.getJSONObject("properties");


                    double mag;
                    String place;
                    long time;
                    String url;

                    //Extract the appropriate values
                    mag = properties.getDouble("mag");
                    place = properties.getString("place");
                    time = properties.getLong("time");
                    url = properties.getString("url");

                    //Add the String values extracted to Earthquake ArrayList
                    earthquakes.add(new Earthquake(mag, place, time, url));
                }
                return earthquakes;
            }

        } catch (JSONException e)
        {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null)
        {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else
            {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e)
        {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset
                    .forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null)
            {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
