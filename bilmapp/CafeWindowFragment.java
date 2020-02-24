package com.example.can.bilmapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;


public class CafeWindowFragment extends Fragment {

    public static ArrayList<String> cafeHours;
    public static ArrayList<Integer> cafeIds;
    public CafeWindowFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cafeIds = new ArrayList<>();
        cafeHours = new ArrayList<>();

        setCafeIds();
        setCafeHours();
        //Changes the pictures and texts for the customized marker
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_cafe_window, container, false);
        Marker marker = MyFragment.selectedMarker;
        EditText editText = rootview.findViewById(R.id.editText);
        editText.setText(marker.getTitle());

        Button menuButton = rootview.findViewById(R.id.button5);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.cafeMenuFragment = new CafeMenuFragment();
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.mainLayout, MainActivity.cafeMenuFragment).commit();
            }
        });

        ImageView cafeImage = rootview.findViewById(R.id.imageView);
        //Using the helper method to get the index
        int markerIndex = MyFragment.getMarkerIndex(marker);
        cafeImage.setImageResource( cafeIds.get( markerIndex ) );

        EditText hoursText = rootview.findViewById(R.id.editText5);
        hoursText.setText( cafeHours.get( markerIndex ) );

        return rootview;
    }
    //Pictures for the cafes in order
    public void setCafeIds()
    {
        cafeIds.add(R.drawable.fc_starbucks);
        cafeIds.add(R.drawable.m_starbucks);
        cafeIds.add(R.drawable.express_1);
        cafeIds.add(R.drawable.coffe_break);
        cafeIds.add(R.drawable.b_mozart);
        cafeIds.add(R.drawable.fiero);
        cafeIds.add(R.drawable.cafein_1);
        cafeIds.add(R.drawable.marmara);
        cafeIds.add(R.drawable.ee_mozart);
        cafeIds.add(R.drawable.speed);
    }
    //Opening closing hours for the cafes in order
    public void setCafeHours()
    {
        cafeHours.add("On weekdays: 07:00 - 20:30\nOn weekends: 09:30 - 17:30");
        cafeHours.add("On weekdays: 07:00 - 20:30\nOn weekends: 09:30 - 17:30");
        cafeHours.add("On weekdays: 08:00 - 18:30\nOn weekends: Closed"); //Express
        cafeHours.add("On weekdays: 08:00 - 17:30\nOn weekends: Closed"); //break
        cafeHours.add("On weekdays: 08:00 - 19:00\nOn weekends: Closed");  //Mozart b
        cafeHours.add("On weekdays: 08:00 - 18:00\nOn weekends: Closed"); //Fiero
        cafeHours.add("On weekdays: 08:00 - 20:00\nOn weekends: 08:00 - 19:30");  //Cafe in
        cafeHours.add("On weekdays: 08:00 - 10:30/11:30 - 14:30/17:00 - 20:00\nOn weekends: 08:00 - 10:30/11:30 - 14:30/17:00 - 20:00");  //Marmara
        cafeHours.add("On weekdays: 08:00 - 18:30\nOn weekends: Closed");  //Mozazrt ee
        cafeHours.add("On weekdays: 11:30 - 21:30/07:30 - 21:30\nOn weekends: 11:30 - 21:30/09:30 - 21:30");
    }

}
