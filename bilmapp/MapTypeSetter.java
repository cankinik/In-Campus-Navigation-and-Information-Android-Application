package com.example.can.bilmapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fasterxml.jackson.databind.type.MapType;
import com.google.android.gms.maps.GoogleMap;


public class MapTypeSetter extends Fragment {

    public MapTypeSetter() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Acquiring the buttons from the rootview and assigning them to their functions
        View rootview = inflater.inflate(R.layout.fragment_map_type_setter, container, false);
        Button satelliteButton = rootview.findViewById(R.id.button19);
        Button normalButton = rootview.findViewById(R.id.button20);
        Button hybridButton = rootview.findViewById(R.id.button21);
        //Map type selection
        satelliteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMapTypeToSatellite(v);
            }
        });
        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMapTypeToNormal(v);

            }
        });
        hybridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMapTypeToHybrid(v);
            }
        });

        return rootview;
    }

    //These three methods interract with MyFragment class to change the map type, using a local variable there
    public void setMapTypeToSatellite(View view)
    {
        if( MainActivity.myFragment == null )
        {
            MainActivity.myFragment = new MyFragment();
        }
        android.support.v4.app.FragmentManager manager = this.getFragmentManager();
        MyFragment.myMapType = (GoogleMap.MAP_TYPE_SATELLITE);
        manager.beginTransaction().replace(R.id.mainLayout, MainActivity.myFragment).commit();
    }
    public void setMapTypeToHybrid(View view)
    {
        if( MainActivity.myFragment == null )
        {
            MainActivity.myFragment = new MyFragment();
        }
        android.support.v4.app.FragmentManager manager = this.getFragmentManager();
        MyFragment.myMapType = (GoogleMap.MAP_TYPE_HYBRID);
        manager.beginTransaction().replace(R.id.mainLayout, MainActivity.myFragment).commit();
    }
    public void setMapTypeToNormal(View view)
    {
        if( MainActivity.myFragment == null )
        {
            MainActivity.myFragment = new MyFragment();
        }
        android.support.v4.app.FragmentManager manager = this.getFragmentManager();
        MyFragment.myMapType = (GoogleMap.MAP_TYPE_NORMAL);
        manager.beginTransaction().replace(R.id.mainLayout, MainActivity.myFragment).commit();
    }
}