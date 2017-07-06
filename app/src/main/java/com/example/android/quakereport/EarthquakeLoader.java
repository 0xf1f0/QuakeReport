package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import java.util.List;

/**
 * Created by Lenovo on 7/6/2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>>
{
    final static String LOG_TAG = EarthquakeLoader.class.getSimpleName();
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
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground()
    {
        List<Earthquake> result = null;

        // Don't perform the request if there are no URLs, or the first URL
        if(getUrl().length() < 1)
            return null;
        try
        {
            // Perform the HTTP request for earthquake data and process the response.
            result = QueryUtils.fetchEarthquakeData(getUrl());

        }catch (SecurityException e)
        {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return result;
    }

    //@return the url of the earthquake data to the displayed
    public String getUrl()
    {
        return mUrl;
    }
}
