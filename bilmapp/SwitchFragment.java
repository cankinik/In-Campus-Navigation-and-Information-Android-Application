package com.example.can.bilmapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


public class SwitchFragment extends Fragment {

    //Switches
    static View rootView;
    static Switch buildingSwitch;
    static Switch cafeSwitch;
    static Switch gymSwitch;
    static Switch parkSwitch;
    //Boolean conditions, initially set to false
    static boolean buildingEnabled = false;
    static boolean cafeEnabled = false;
    static boolean gymEnabled = false;
    static boolean parkEnabled = false;
    //Empty constructor (Mandatory)
    public SwitchFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating the view for the fragment to return
        rootView = inflater.inflate(R.layout.fragment_switch, container, false);
        //Initializing the switches through the rootView
        buildingSwitch = (Switch) rootView.findViewById(R.id.switch5);
        cafeSwitch = (Switch) rootView.findViewById(R.id.switch6);
        gymSwitch = (Switch) rootView.findViewById(R.id.switch7);
        parkSwitch = (Switch) rootView.findViewById(R.id.switch8);
        //Setting up the listeners for the switches
        buildingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                        buildingEnabled = true;
                } else {
                       buildingEnabled = false;
                }
            }
        });
        cafeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cafeEnabled = true;
                } else {
                    cafeEnabled = false;
                }
            }
        });
        gymSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gymEnabled = true;
                } else {
                    gymEnabled = false;
                }
            }
        });
        parkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    parkEnabled = true;
                } else {
                    parkEnabled = false;
                }
            }
        });
        return rootView;
    }

    public static boolean isBuildingsEnabled()
    {
        return buildingEnabled;
    }
    public static boolean isCafesEnabled()
    {

        return cafeEnabled;
    }
    public static boolean isGymsEnabled()
    {

        return gymEnabled;
    }
    public static boolean isParksEnabled()
    {

        return parkEnabled;
    }

}
