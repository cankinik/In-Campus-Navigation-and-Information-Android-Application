package com.example.can.bilmapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;


public class CafeMenuFragment extends Fragment {

    public static ArrayList<Integer> menuIds;
    public static int index;
    public CafeMenuFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setMenuIds();
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_cafe_menu, container, false);
        ImageView menu = rootview.findViewById(R.id.imageView3);
        //Using the helper method to reach the index
        menu.setImageResource(menuIds.get( MyFragment.getMarkerIndex(MyFragment.selectedMarker) ));

        return rootview;
    }
    //Setting the cafe menus in correct index
    public void setMenuIds()
    {
        menuIds = new ArrayList<>();
        menuIds.add(R.drawable.starbucks_menu);
        menuIds.add(R.drawable.starbucks_menu);
        menuIds.add(R.drawable.starbucks_menu);
        menuIds.add(R.drawable.starbucks_menu);
        menuIds.add(R.drawable.b_mozart_menu);
        menuIds.add(R.drawable.fiero_menu);
        menuIds.add(R.drawable.starbucks_menu);
        menuIds.add(R.drawable.starbucks_menu);
        menuIds.add(R.drawable.mozart_ee_menu);
        menuIds.add(R.drawable.starbucks_menu);
    }

}