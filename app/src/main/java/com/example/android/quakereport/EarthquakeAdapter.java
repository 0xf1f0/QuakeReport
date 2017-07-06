package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Lenovo on 6/25/2017.
 */


class EarthquakeAdapter extends ArrayAdapter<Earthquake>
{

    private static final String LOCATION_SEPARATOR = " of ";

    //Overloaded constructor

    /***
     *
     * @param context is the current context where the object is being instantiated e.g this
     * @param earthquakes is the name of the ArrayList of object Earthquake
     */
    EarthquakeAdapter(Activity context, List<Earthquake> earthquakes)
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
        String formattedMagnitude = null;
        if (earthquake != null)
        {
            formattedMagnitude = formatMagnitude(earthquake.getMagnitude());
        }
        earthquakeMagnitude.setText(formattedMagnitude);

        //Split the location into offset and primary locations
        String location = null;
        if (earthquake != null)
        {
            location = earthquake.getLocation();
        }
        String offsetLocation = null;
        String primaryLocation = null;

        if (location != null)
        {
            if (location.contains(LOCATION_SEPARATOR)) {
                String[] parts = location.split(LOCATION_SEPARATOR);
                offsetLocation = parts[0] + LOCATION_SEPARATOR;
                primaryLocation = parts[1];
            } else {
                offsetLocation = "Near the";
                primaryLocation = location;
            }
        }

        //Find the TextView with the view ID offset_location
        TextView earthquakeOffsetLocation = (TextView) listEarthquakeView.findViewById(R.id
                .offset_location);
        earthquakeOffsetLocation.setText(offsetLocation);

        //Find the TextView with the view ID primary_location
        TextView earthquakePrimaryLocation = (TextView) listEarthquakeView.findViewById(R.id
                .primary_location);
        earthquakePrimaryLocation.setText(primaryLocation);

        /* Create a new Date object from the time in milliseconds of the earthquake */
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

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) earthquakeMagnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        //

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

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude)
    {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    /**
     * Return the color for the appropriate earthquake magnitude
     *
     */
    private int getMagnitudeColor (double magnitude)
    {
        int magnitudeCOlorResourceId;
        //Get the floor value of the magnitude(i.e 6.2 becomes 6)
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor)
        {
            case 0:
            case 1:
                magnitudeCOlorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeCOlorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeCOlorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeCOlorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeCOlorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeCOlorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeCOlorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeCOlorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeCOlorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeCOlorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeCOlorResourceId);
    }
}
