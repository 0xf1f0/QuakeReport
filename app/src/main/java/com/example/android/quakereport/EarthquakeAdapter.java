package com.example.android.quakereport;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lenovo on 6/25/2017.
 */


public class EarthquakeAdapter extends ArrayAdapter<Earthquake>
{
    //Overloaded constructor

    /***
     *
     * @param context is the current context where the object is being instantiated e.g this
     * @param earthquakes is the name of the ArrayList of object Earthquake
     */
    public  EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes)
    {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listEarthquakeView = convertView;

        if(listEarthquakeView ==  null)
        {
            listEarthquakeView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        //Create an object of an earthquake
        Earthquake earthquake = getItem(position);

        //Find the TextView with the view ID magnitude
        TextView earthquakeMagnitude = (TextView) listEarthquakeView.findViewById(R.id.magnitude);
        earthquakeMagnitude.setText(earthquake.getMagnitude());

        //Find the TextView with the view ID location
        TextView earthquakeLocation = (TextView) listEarthquakeView.findViewById(R.id.location);
        earthquakeLocation.setText(earthquake.getLocation());

        //Find the TextView with the view ID date
        TextView earthquakeDate = (TextView) listEarthquakeView.findViewById(R.id.date);
        earthquakeDate.setText(earthquake.getDate());

        return listEarthquakeView;
    }
}
