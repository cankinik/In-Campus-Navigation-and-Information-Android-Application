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


public class GymWindowFragment extends Fragment {

    public static ArrayList<Integer> gymIds;
    public GymWindowFragment() {
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
        gymIds = new ArrayList<>();

        setGymIds();
        View rootview = inflater.inflate(R.layout.fragment_gym_window, container, false);
        Marker marker = MyFragment.selectedMarker;
        //Using the helper method to get the index of the sport center
        ImageView buildingImage = rootview.findViewById(R.id.imageView4);
        int markerIndex = MyFragment.getMarkerIndex(marker);
        buildingImage.setImageResource( gymIds.get( markerIndex ) );

        EditText editText = rootview.findViewById(R.id.editText2);
        editText.setText(marker.getTitle());
        return rootview;
    }
    //Setting the images in order
    public void setGymIds()
    {
        gymIds.add(R.drawable.dorm_gym);
        gymIds.add(R.drawable.main_gym);
    }
}
