package com.example.can.bilmapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class BuildingWindowFragment extends Fragment {

    public static ArrayList<Integer> buildingIds;
    public BuildingWindowFragment() {
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
        buildingIds = new ArrayList<>();

        setBuildingIds();
        View rootView = inflater.inflate(R.layout.fragment_building_window, container, false);
        //Changing the selectedMarker property of the map fragment
        Marker marker = MyFragment.selectedMarker;
        EditText editText = rootView.findViewById(R.id.editText2);
        editText.setText(marker.getTitle());

        ImageView buildingImage = rootView.findViewById(R.id.imageView4);
        int markerIndex = MyFragment.getMarkerIndex(marker);
        //Using the helper method to get the index of the marker in the decleration
        buildingImage.setImageResource( buildingIds.get( markerIndex ) );

       return rootView;
    }
    //Setting the building pictures according to their index of decleration in the setBuildings method in the MyFragment class
    public void setBuildingIds()
    {
        buildingIds.add(R.drawable.sb);
        buildingIds.add(R.drawable.sa);
        buildingIds.add(R.drawable.b);
        buildingIds.add(R.drawable.g);
        buildingIds.add(R.drawable.library);
        buildingIds.add(R.drawable.ee);
        buildingIds.add(R.drawable.ah);
        buildingIds.add(R.drawable.fc);
        buildingIds.add(R.drawable.ff);
        buildingIds.add(R.drawable.m);
    }
}
