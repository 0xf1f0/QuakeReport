package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Lenovo on 7/6/2017.
 */

/*
   Loads a list of earthquakes by using an AsyncTaskLoader to perform the
   network request to the destination(USGS) url.
 */
public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>>
{
    /* Tag for log messages */
    private final static String LOG_TAG = EarthquakeLoader.class.getSimpleName();

    /* Query URL */
    private String mUrl;

    //TODO: Extract the earthquake data from the USGS url
    public EarthquakeLoader(Context context, String url)
    {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading()
    {
        Log.i(LOG_TAG, "-> Calling: onStartLoading");
        forceLoad();
    }

    /*
     * Run the earthquake data request in the background
     */
    @Override
    public List<Earthquake> loadInBackground()
    {
        Log.i(LOG_TAG, "-> Calling: loadInBackground");
        List<Earthquake> earthquakes = null;

        // Don't perform the request if there are no URLs, or the first URL
        if (mUrl == null)
            return null;
        try
        {
            /* Perform the HTTP request for earthquake data and process the response. */
            earthquakes = QueryUtils.fetchEarthquakeData(mUrl);

        }
        /*
            In case internet permission is not added to AndroidManifest.xml
         */
        catch (SecurityException e)
        {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return earthquakes;
    }
}
