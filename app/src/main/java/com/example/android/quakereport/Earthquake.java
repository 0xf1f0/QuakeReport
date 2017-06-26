package com.example.android.quakereport;

/**
 * Created by Lenovo on 6/25/2017.
 */

public class Earthquake
{
    //Magnitude of the earthquake
    private String mMagnitude;

    //Location of the earthquake
    private String mLocation;

    //Date of the earthquake
    private String mDate;


    /***
     * @param magnitude is the magnitude of the earthquake
     * @param location is the city location of the earthquake
     * @param date is the date the earthquake occurred
     */
    public Earthquake (String magnitude, String location, String date)
    {
        mMagnitude = magnitude;
        mLocation = location;
        mDate = date;
    }


    /**
     * @return the magnitude of an earthquake
     */
    public String getMagnitude()
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
    public String getDate()
    {
        return mDate;
    }
}
