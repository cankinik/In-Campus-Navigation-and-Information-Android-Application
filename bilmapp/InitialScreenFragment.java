package com.example.can.bilmapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class InitialScreenFragment extends Fragment {


    public InitialScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // The initial screen, presenting the options in the drawer as well for easier use and utilized as a welcoming page
        return inflater.inflate(R.layout.fragment_initial_screen, container, false);
    }


}