package com.example.android.quakereport;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Lenovo on 6/25/2017.
 */


public class EarthquakeAdapter extends ArrayAdapter<Earthquake>
{

    private static final String LOCATION_SEPARATOR = " of ";

    //Overloaded constructor

    /***
     *
     * @param context is the current context where the object is being instantiated e.g this
     * @param earthquakes is the name of the ArrayList of object Earthquake
     */
    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes)
    {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listEarthquakeView = convertView;

        if (listEarthquakeView == null) {
            listEarthquakeView = LayoutInflater.from(getContext()).inflate(R.layout
                    .earthquake_list_item, parent, false);
        }

        //Create an object of an earthquake
        Earthquake earthquake = getItem(position);

        //Find the TextView with the view ID magnitude
        TextView earthquakeMagnitude = (TextView) listEarthquakeView.findViewById(R.id.magnitude);
        earthquakeMagnitude.setText(earthquake.getMagnitude());

        //Split the location into offset and primary locations
        String location = earthquake.getLocation();
        String offsetLocation;
        String primaryLocation;

        if (location.contains(LOCATION_SEPARATOR))
        {
            String[] parts = location.split(LOCATION_SEPARATOR);
            offsetLocation = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        }
        else
        {
            offsetLocation = "Near the";
            primaryLocation = location;
        }

        //Find the TextView with the view ID offset_location
        TextView earthquakeOffsetLocation = (TextView) listEarthquakeView.findViewById(R.id
                .offset_location);
        earthquakeOffsetLocation.setText(offsetLocation);

        //Find the TextView with the view ID primary_location
        TextView earthquakPrimaryLocation = (TextView) listEarthquakeView.findViewById(R.id
                .primary_location);
        earthquakPrimaryLocation.setText(primaryLocation);

        //Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(earthquake.getTimeInMilliseconds());

        //Format the time(ms) to display a readable date( i.e Mar 3, 2010)
        String formattedDate = formatDate(dateObject);

        //Format the time(ms) to display a readable time(i.e 4:30 PM)
        String formattedTime = formatTime(dateObject);

        //Find the TextView with the view ID date
        TextView earthquakeDate = (TextView) listEarthquakeView.findViewById(R.id.date);
        earthquakeDate.setText(formattedDate);

        //Find the TextView with the view ID date
        TextView earthquakeTime = (TextView) listEarthquakeView.findViewById(R.id.time);
        earthquakeTime.setText(formattedTime);

        return listEarthquakeView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */

    private String formatDate(Date dateObject)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject)
    {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
