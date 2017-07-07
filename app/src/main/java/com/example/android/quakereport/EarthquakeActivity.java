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
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<List<Earthquake>>
{
    // Create a new {@link ArrayAdapter} of earthquakes
    private EarthquakeAdapter mAdapter;
    private static final int LOADER_ID = 1;
    private TextView emptyStatTextView;
    private ProgressBar progressBar;

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

        //Display the earthquake data
        updateUI();
        //Initialize the LoaderManager
        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    private void updateUI()
    {
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        //Create an OnClickListener to launch a web browser intent when an
        //earthquake list view is clicked
        // Set the mAdapter on the {@link ListView}
        // so the list can be populated in the user interface
        if (earthquakeListView != null)
        {
            earthquakeListView.setAdapter(mAdapter);
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
            //Set an empty view if no earthquake data is available
            emptyStatTextView = (TextView)findViewById(R.id.empty_state);
            earthquakeListView.setEmptyView(emptyStatTextView);
        }

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle)
    {
        Log.i(LOG_TAG, "-> Calling: onCreateLoader");
        // TODO: Create a new loader for the given URL
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes)
    {
        //Hide the progress bar
        progressBar.setVisibility(View.GONE);

        //Set the text in the empty state Text_View at the first load
        emptyStatTextView.setText(R.string.no_earthquake);

        //Clear the adapter of previous earthquake data
        mAdapter.clear();

        Log.i(LOG_TAG, "-> Calling: onLoadFinished");
        // If there is earthquake, do nothing
        if (earthquakes != null && !earthquakes.isEmpty())
        {
            mAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader)
    {
        Log.i(LOG_TAG, "-> Calling: onLoaderReset");
        // TODO: Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
