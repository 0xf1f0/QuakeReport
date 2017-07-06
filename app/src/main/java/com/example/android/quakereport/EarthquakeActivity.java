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

import android.content.Intent;
import android.net.Uri;
import android.net.sip.SipException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    //TODO: Extract the earthquake data from the USGS url

    final static String USGS_REQUEST_URL = "https://earthquake.usgs" +
            ".gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6" +
            "&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        //Execute the Async task
        EarthquakeAsyncTask earthquakeAsyncTask = new EarthquakeAsyncTask();
        earthquakeAsyncTask.execute(USGS_REQUEST_URL);
    }

    private void updateUI(final List<Earthquake> earthquakes)
    {
        // Create a new {@link ArrayAdapter} of     earthquakes
        EarthquakeAdapter adapter = new EarthquakeAdapter(EarthquakeActivity.this, earthquakes);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        //Create an OnClickListener to launch a web browser intent when an
        //earthquake list view is clicked

        if (earthquakeListView != null)
        {
            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    //Get the {@link earthquake } object at the given position(i) the user clicked on
                    Earthquake earthquake = earthquakes.get(i);

                    //Convert String into a Uri Object
                    Uri earthquakeUri = Uri.parse(earthquakes.get(i).getUrl());

                    //Create an intent that explicitly launches the url at the current position
                    //Launch an intent when user clicks on an earthquake view
                    Intent earthquakeWebsite = new Intent(Intent.ACTION_VIEW);
                    earthquakeWebsite.setData(earthquakeUri);
                    startActivity(earthquakeWebsite);
                }
            });

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter(adapter);
        }
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>>
    {

        @Override
        protected List<Earthquake> doInBackground(String... urls) throws SecurityException
        {
            List<Earthquake> result = null;
            // Don't perform the request if there are no URLs, or the first URL
            if(urls.length < 1 || urls[0] == null)
            {
                return null;
            }
            try
            {
                // Perform the HTTP request for earthquake data and process the response.
                result = QueryUtils.fetchEarthquakeData(urls[0]);

            }catch (SecurityException e)
            {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            return result;
        }


        @Override
        protected void onPostExecute(List<Earthquake> result)
        {
            // If there is no result, do nothing.
            if(result == null)
            {
                return;
            }
            updateUI(result);
        }

    }
}
