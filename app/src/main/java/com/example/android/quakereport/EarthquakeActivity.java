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
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
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


public class EarthquakeActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<List<Earthquake>>
{
    // Create a new {@link ArrayAdapter} of earthquakes
    private EarthquakeAdapter mAdapter;
    private static final int LOADER_ID = 1;
    private TextView emptyStateTextView;
    private ListView earthquakeListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View progressBar;
    private Boolean deviceHasInternet;
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    final static String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(LOG_TAG, "-> Calling: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list);

        // Find a reference to the {@link SwipeRefreshLayout} in the layout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        //Set an empty view if no earthquake data is available
        emptyStateTextView = (TextView) findViewById(R.id.empty_state);

        //Update the empty state with the no connection error message
        earthquakeListView.setEmptyView(emptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        //Link the progress bar
        progressBar = findViewById(R.id.progressBar);

        //check device's internet connectivity
        getDeviceHasInternet();

        // Set the mAdapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        //Create an OnClickListener to launch a web browser intent when an
        //earthquake list view is clicked
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //Get the {@link earthquake } object at the given position(i) the user
                // clicked on
                Earthquake earthquake = mAdapter.getItem(i);

                //Convert String into a Uri Object
                Uri earthquakeUri = null;
                if (earthquake != null)
                {
                    earthquakeUri = Uri.parse(earthquake.getUrl());
                }

                //Create an intent that explicitly launches the url at the current position
                //Launch an intent when user clicks on an earthquake view
                Intent earthquakeWebsite = new Intent(Intent.ACTION_VIEW);
                earthquakeWebsite.setData(earthquakeUri);
                startActivity(earthquakeWebsite);
            }
        });


        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                Log.i(LOG_TAG, "-> Calling: Refresh swiped");

                //Update the UI
                updateUI();
            }
        });

        //Update the UI
        updateUI();
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle)
    {
        Log.i(LOG_TAG, "-> Calling: onCreateLoader");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes)
    {
        Log.i(LOG_TAG, "-> Calling: onLoadFinished");
        // If there is earthquake, do nothing
        if (earthquakes != null && !earthquakes.isEmpty())
        {
            mAdapter.addAll(earthquakes);

            //Hide the progress bar from view once there are earthquake data to be displayed
            if (progressBar != null && !mAdapter.isEmpty())
                progressBar.setVisibility(View.GONE);
        }

        Log.i(LOG_TAG, "-> Calling: mAdapter is empty: " + mAdapter.isEmpty());
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader)
    {
        Log.i(LOG_TAG, "-> Calling: onLoaderReset");

        // TODO: Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    /***
     * @return the internet connectivity status of the android device
     */
    private Boolean getDeviceHasInternet()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        deviceHasInternet = networkInfo != null && networkInfo.isConnected();
        return deviceHasInternet;
    }

    //Update the user interface based on action performed by the user and device's status
    private void updateUI()
    {
        Log.i(LOG_TAG, "-> Calling: updateUI");
        Log.i(LOG_TAG, "-> Device has internet: " + getDeviceHasInternet());
        //Check for internet connectivity
        if (getDeviceHasInternet())
        {
            LoaderManager listLoader = getLoaderManager();
            listLoader.initLoader(LOADER_ID, null, this);
        } else
        {
            //Display the empty state only when connectivity is false and
            //Loader Manager has not been initialized
            displayEmptyState();

        }
        //Update swipe refresh status
        swipeRefreshLayout.setRefreshing(false);

    }

    private void displayEmptyState()
    {
        Log.i(LOG_TAG, "-> Calling: displayEmptyState");
        //Hide the progress bar from view
        progressBar.setVisibility(View.GONE);

        //Clear the adapter of previous earthquake data
        mAdapter.clear();
        //Set the text in the empty state Text_View at the first load
        emptyStateTextView.setText(R.string.no_internet_connection);
    }

    //Make a preference screen


    // Inflate the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_settings)
        {
            //Create a new intent for the settings activity
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
