package com.example.can.bilmapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;


public class ParkWindowFragment extends Fragment {

    public static ArrayList<String> parkEligibilities;
    public static ArrayList<Integer> parkIds;
    public ParkWindowFragment() {
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

        parkIds = new ArrayList<>();
        setParkIds();

        parkEligibilities = new ArrayList<>();
        setParkEligibilities();

        View rootview = inflater.inflate(R.layout.fragment_park_window, container, false);
        Marker marker = MyFragment.selectedMarker;
        EditText editText = rootview.findViewById(R.id.editText2);
        editText.setText(marker.getTitle());



        ImageView cafeImage = rootview.findViewById(R.id.imageView4);
        int markerIndex = MyFragment.getMarkerIndex(marker);
        cafeImage.setImageResource( parkIds.get( markerIndex ) );

        rootview.findViewById(R.id.editText6);
        editText.setText( parkEligibilities.get( markerIndex ) );

        return rootview;
    }
    public void setParkIds()
    {
        parkIds.add(R.drawable.mescit_park);
        parkIds.add(R.drawable.ee_park);
        parkIds.add(R.drawable.unam_1);
        parkIds.add(R.drawable.meteksan_park);
    }
    public void setParkEligibilities()
    {
        parkEligibilities.add("Open access for all");
        parkEligibilities.add("Open access for all");
        parkEligibilities.add("Eligible for staff only");
        parkEligibilities.add("Eligible for staff only");

    }
}
