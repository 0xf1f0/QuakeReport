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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.quakereport.R.id.progressBar;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<List<Earthquake>>
{
    // Create a new {@link ArrayAdapter} of earthquakes
    private EarthquakeAdapter mAdapter;
    private static final int LOADER_ID = 1;
    private TextView emptyStateTextView;
    private ListView earthquakeListView;
    private View progressBar;
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    final static String USGS_REQUEST_URL = "https://earthquake.usgs" +
            ".gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6" +
            "&limit=10";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(LOG_TAG, "-> Calling: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list);

        //Set an empty view if no earthquake data is available
        emptyStateTextView = (TextView) findViewById(R.id.empty_state);

        //Update the empty state with the no connection error message
        earthquakeListView.setEmptyView(emptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        //Link the progress bar
        progressBar = findViewById(R.id.progressBar);

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
                Uri earthquakeUri = Uri.parse(mAdapter.getItem(i).getUrl());

                //Create an intent that explicitly launches the url at the current position
                //Launch an intent when user clicks on an earthquake view
                Intent earthquakeWebsite = new Intent(Intent.ACTION_VIEW);
                earthquakeWebsite.setData(earthquakeUri);
                startActivity(earthquakeWebsite);
            }
        });

        //Check for internet connectivity
        if (deviceHasInternetConnectivity())
        {
            //Initialize the LoaderManager
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }
        else
        {
            //Hide the progress bar from view
            progressBar.setVisibility(View.GONE);
            //Set the text in the empty state Text_View at the first load
            emptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle)
    {
        Log.i(LOG_TAG, "-> Calling: onCreateLoader");
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes)
    {
        //Clear the adapter of previous earthquake data
        mAdapter.clear();

        Log.i(LOG_TAG, "-> Calling: onLoadFinished");
        // If there is earthquake, do nothing
        if (earthquakes != null && !earthquakes.isEmpty())
        {
            mAdapter.addAll(earthquakes);

            //Hide the progress bar from view once there are earthquake data to be displayed
            if(progressBar != null && !mAdapter.isEmpty())
                progressBar.setVisibility(View.GONE);
        }
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
    private Boolean deviceHasInternetConnectivity()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
