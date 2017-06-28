package com.example.android.quakereport;

/**
 * Created by Lenovo on 6/25/2017.
 */

public class Earthquake
{
    //Magnitude of the earthquake
    private double mMagnitude;

    //Location of the earthquake
    private String mLocation;

    //Date of the earthquake
    private long mTimeInMilliseconds;


    /***
     * @param magnitude is the magnitude of the earthquake
     * @param location is the city location of the earthquake
     * @param timeInMilliseconds is the date the earthquake occurred
     */
    public Earthquake (double magnitude, String location, long timeInMilliseconds)
    {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
    }


    /**
     * @return the magnitude of an earthquake
     */
    public double getMagnitude()
    {
        return mMagnitude;
    }


    /**
     * @return the location of an earthquake
     */
    public String getLocation()
    {
        return mLocation;
    }

    /**
     * @return the date of an earthquake
     */
    public long getTimeInMilliseconds()
    {
        return mTimeInMilliseconds;
    }
}
