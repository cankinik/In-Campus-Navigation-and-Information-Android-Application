package com.example.can.bilmapp;


import android.content.Context;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.type.MapType;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;

/**
 * Fragment for the map in the application
 * @author Can KINIK
 * @version 05.07.2018
 */

public class MyFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    //Variables
    public static GoogleMap mMap;
    //Marker stuff for the user
    MarkerOptions markerOptions;
    public static Marker myMarker;
    LocationManager locationManager;
    public static LatLng myCoordinates;
    //Marker stuff for buildings
    public static ArrayList<Marker> buildingMarkers = new ArrayList<>();
    ArrayList<MarkerOptions> buildingMarkerOptions = new ArrayList<>();
    ArrayList<LatLng> buildingLocations = new ArrayList<>();
    ArrayList<String> buildingNames = new ArrayList<>();
    //Marker stuff for cafes
    public static ArrayList<Marker> cafeMarkers;
    ArrayList<MarkerOptions> cafeMarkerOptions;
    ArrayList<LatLng> cafeLocations;
    ArrayList<String> cafeNames;
    //Marker stuff for gyms
    public static ArrayList<Marker> gymMarkers;
    ArrayList<MarkerOptions> gymMarkerOptions;
    ArrayList<LatLng> gymLocations;
    ArrayList<String> gymNames;
    //Marker stuff for gyms
    public static ArrayList<Marker> parkMarkers;
    ArrayList<MarkerOptions> parkMarkerOptions;
    ArrayList<LatLng> parkLocations;
    ArrayList<String> parkNames;
    //Polyline to draw the directions
    public static Polyline polyline;
    public static PolylineOptions polylineOptions;
    public static Marker selectedMarker;
    //Just an integer that is not viable for Google.MAP_TYPE, since integers cannot be null, this had to be implemented
    public static int myMapType = -13;

    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Changed here for the fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Initializing the mMap object, setting the map type and enabling the buildings
        mMap = googleMap;
        if( myMapType == -13 )
        {
            myMapType = GoogleMap.MAP_TYPE_NORMAL;
        }
        mMap.setMapType(myMapType);

        //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.setBuildingsEnabled(true);
        //Initially setting my location to a coordinate in the campus, adding the marker for this location and naming it as "Me!"
        myCoordinates = new LatLng(39.868574, 32.748729);
        markerOptions = new MarkerOptions();
        markerOptions.position(myCoordinates).title("Me!");
        myMarker = mMap.addMarker(markerOptions);
        selectedMarker = myMarker;
        //Zooming the camera to myCoordinates and setting the markers for the buildings, cafes, gyms and parking lots
        centreOnMarker(selectedMarker);

        //Creating the markers
        setBuildingMarkers();
        setCafeMarkers();
        setGymMarkers();
        setParkMarkers();
        //Hiding the markers according to the SwitchFragment
        if( !SwitchFragment.isBuildingsEnabled() )
        {
            setMarkersHidden(buildingMarkers);
        }
        if( !SwitchFragment.isCafesEnabled() )
        {
            setMarkersHidden(cafeMarkers);
        }
        if( !SwitchFragment.isGymsEnabled() )
        {
            setMarkersHidden(gymMarkers);
        }
        if( !SwitchFragment.isParksEnabled() )
        {
            setMarkersHidden(parkMarkers);
        }

        //Setting the map bound to Ankara (very roughly)
        LatLngBounds bounds = new LatLngBounds(new LatLng(39.737167, 32.528614), new LatLng(40.076898, 33.009266));
        mMap.setLatLngBoundsForCameraTarget(bounds);
        //Initially the polyline is set to null to check for eligibility of using .remove() method
        polyline = null;

        //A customized info-window is shown, depending on the type of the marker
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }
            //The InfoContent to be used
            @Override
            public View getInfoContents(Marker marker) {
                View view;
                if( marker != myMarker )
                {
                    view = getLayoutInflater().inflate(R.layout.info_window_0, null);
                    TextView editText = view.findViewById(R.id.editText2);
                    editText.setText(marker.getTitle());

                }
                else
                {
                    view = getLayoutInflater().inflate(R.layout.my_info_window, null);
                    TextView editText = view.findViewById(R.id.editText3);
                    editText.setText(marker.getTitle());
                }
                return view;
            }

        });
        //Long and short click listeners
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                selectedMarker = marker;
                //Opens up the corresponding fragment according to the type of the marker that is interracted with
                if( buildingMarkers.contains(marker) )
                {
                    BuildingWindowFragment buildingWindowFragment = new BuildingWindowFragment();
                    getFragmentManager().beginTransaction().replace(R.id.mainLayout, buildingWindowFragment).commit();
                }
                else if( cafeMarkers.contains(marker) )
                {
                    CafeWindowFragment cafeWindowFragment = new CafeWindowFragment();
                    getFragmentManager().beginTransaction().replace(R.id.mainLayout, cafeWindowFragment).commit();
                }
                else if( gymMarkers.contains(marker) )
                {
                    GymWindowFragment gymWindowFragment = new GymWindowFragment();
                    getFragmentManager().beginTransaction().replace(R.id.mainLayout, gymWindowFragment).commit();
                }
                else if( parkMarkers.contains(marker) )
                {
                    ParkWindowFragment parkWindowFragment = new ParkWindowFragment();
                    getFragmentManager().beginTransaction().replace(R.id.mainLayout, parkWindowFragment).commit();
                }

            }
        });
        mMap.setOnInfoWindowClickListener(this);


    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        //Centering on the clicked marker and zooming in with level 17
        centreOnMarker(marker);
        LatLng coordinates = marker.getPosition();
        if( marker != myMarker )
        {
            //Clearing the previous line
            if( polyline != null )
            {
                polyline.remove();
            }
            drawDirectionsToMarker(marker);
        }

    }



    //Marker visibility methods
    public static void setMarkersHidden(ArrayList<Marker> markersToBeSetVisible)
    {
        for(int i = 0; i < markersToBeSetVisible.size(); i++)
        {
            markersToBeSetVisible.get(i).setVisible(false);
        }
    }
    public void setMarkersVisible(ArrayList<Marker> markersToBeSetVisible)
    {
        for(int i = 0; i < markersToBeSetVisible.size(); i++)
        {
            markersToBeSetVisible.get(i).setVisible(true);
        }
    }
    //Marker helper methods
    public static void centreOnMarker(Marker marker)
    {
        LatLng markerLocation = marker.getPosition();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation,18));
    }
    public static ArrayList<Marker> determineMarkerType(Marker marker)
    {
        if( buildingMarkers.contains(marker) )
        {
            return buildingMarkers;
        }
        else if ( cafeMarkers.contains(marker) )
        {
            return cafeMarkers;
        }
        else if ( gymMarkers.contains(marker) )
        {
            return gymMarkers;
        }
        else if ( parkMarkers.contains(marker) )
        {
            return parkMarkers;
        }
        else
        {
            return null;
        }
    }
    //Returns the index of the marker in its category of the marker ArrayList -> buildingMarkers, cafeMarkers etc.
    public static int getMarkerIndex(Marker marker)
    {
        ArrayList<Marker> markerType = determineMarkerType(marker);
        return markerType.indexOf(marker);
    }
    //Method for drawing the polyline to direct the user to a marker
    public static void drawDirectionsToMarker(Marker marker)
    {
        String url = getRequestUrl(marker);
        MyFragment.TaskRequestDirections taskRequestDirections = new MyFragment.TaskRequestDirections();
        taskRequestDirections.execute(url);
    }
    //Returns the marker that has a corrseponding title to the argument String
    public static Marker getMarkerByName(String name)
    {
        Marker result = myMarker;
        for(int i = 0; i < buildingMarkers.size(); i++)
        {
            if( name.equals( buildingMarkers.get(i).getTitle() ) )
            {
                result = buildingMarkers.get(i);
            }
        }
        return result;
    }
    //Setting the markers
    public void setCafeMarkers()
    {
        cafeMarkers = new ArrayList<Marker>();
        cafeMarkerOptions = new ArrayList<MarkerOptions>();
        cafeLocations = new ArrayList<LatLng>();
        cafeNames = new ArrayList<String>();

        cafeNames.add("GSF Starbucks");
        cafeNames.add("Starbucks");
        cafeNames.add("Cafe Express");
        cafeNames.add("Coffee Break");
        cafeNames.add("Mozart Cafe B");
        cafeNames.add("Fiero Cafe");
        cafeNames.add("Cafe In");
        cafeNames.add("Marmara Restaurant");
        cafeNames.add("Mozart Cafe EE");
        cafeNames.add("Speed Cafe");

        cafeLocations.add(new LatLng(39.866447, 32.749166));//GSF Starbucks 0
        cafeLocations.add(new LatLng(39.867181, 32.749909));//Starbucks 1
        cafeLocations.add(new LatLng(39.867268, 32.748382));//Cafe Express 2
        cafeLocations.add(new LatLng(39.868234, 32.749118));//Coffee Break 3
        cafeLocations.add(new LatLng(39.868942, 32.748162));//Mozart Cafe B 4
        cafeLocations.add(new LatLng(39.868170, 32.749645));//Fiero Cafe 5
        cafeLocations.add(new LatLng(39.870016, 32.750586));//Cafe In 6
        cafeLocations.add(new LatLng(39.870680, 32.750582));//Marmara Restaurant 7
        cafeLocations.add(new LatLng(39.872147, 32.751037));//Mozart Cafe EE 8
        cafeLocations.add(new LatLng(39.866333, 32.748235));//Speed Cafe 9
        //Creating marker options with the locations and names
        for(int i = 0; i < cafeLocations.size(); i++)
        {
            cafeMarkerOptions.add(new MarkerOptions()
                    .position(cafeLocations.get(i))
                    .title(cafeNames.get(i))
                    //Need to change the icon later
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_cafe))
            );
        }
        //Creating markers with the marker options and putting them on the map
        for(int i = 0; i < cafeMarkerOptions.size(); i++)
        {
            cafeMarkers.add(mMap.addMarker(cafeMarkerOptions.get(i)));
        }
    }
    public void setBuildingMarkers()
    {
//        buildingMarkers = new ArrayList<Marker>();
//        buildingMarkerOptions = new ArrayList<MarkerOptions>();
//        buildingLocations = new ArrayList<LatLng>();
//        buildingNames = new ArrayList<String>();

        buildingNames.add("SB");
        buildingNames.add("SA");
        buildingNames.add("B");
        buildingNames.add("G");
        buildingNames.add("Library");
        buildingNames.add("EE");
        buildingNames.add("A/H");
        buildingNames.add("FC");
        buildingNames.add("FF");
        buildingNames.add("M");//Isletme

        buildingLocations.add(new LatLng(39.868316, 32.748142));//SB 0
        buildingLocations.add(new LatLng(39.867712, 32.748203));//SA 1
        buildingLocations.add(new LatLng(39.868800, 32.748076));//B 2
        buildingLocations.add(new LatLng(39.868718, 32.749551));//G 3
        buildingLocations.add(new LatLng(39.870336, 32.749476));//Library 4
        buildingLocations.add(new LatLng(39.872086, 32.750812));//EE 5
        buildingLocations.add(new LatLng(39.867925, 32.749464));//A/H 6
        buildingLocations.add(new LatLng(39.866680, 32.749425));//FC 7
        buildingLocations.add(new LatLng(39.865887, 32.748885));//FF 8
        buildingLocations.add(new LatLng(39.867407, 32.750154));//Isletme 9
        //Creating marker options with the locations and names
        for(int i = 0; i < buildingLocations.size(); i++)
        {
            buildingMarkerOptions.add(new MarkerOptions()
                    .position(buildingLocations.get(i))
                    .title(buildingNames.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name_buildings))
            );
        }
        //Creating markers with the marker options and putting them on the map
        for(int i = 0; i < buildingMarkerOptions.size(); i++)
        {
            buildingMarkers.add(mMap.addMarker(buildingMarkerOptions.get(i)));
        }
    }
    public void setGymMarkers()
    {
        gymMarkers = new ArrayList<Marker>();
        gymMarkerOptions = new ArrayList<MarkerOptions>();
        gymLocations = new ArrayList<LatLng>();
        gymNames = new ArrayList<String>();

        gymNames.add("Student Dormitories Sports Hall");
        gymNames.add("Physical Education Sports Center");


        gymLocations.add(new LatLng(39.863847, 32.745615));//Student Dormitories Sports Hall 0
        gymLocations.add(new LatLng(39.866718, 32.748308));//Physical Education Sports Center 1

        //Creating marker options with the locations and names
        for(int i = 0; i < gymLocations.size(); i++)
        {
            gymMarkerOptions.add(new MarkerOptions()
                    .position(gymLocations.get(i))
                    .title(gymNames.get(i))
                    //Need to change the icon later
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_gym))
            );
        }
        //Creating markers with the marker options and putting them on the map
        for(int i = 0; i < gymMarkerOptions.size(); i++)
        {
            gymMarkers.add(mMap.addMarker(gymMarkerOptions.get(i)));
        }
    }
    public void setParkMarkers()
    {
        parkMarkers = new ArrayList<Marker>();
        parkMarkerOptions = new ArrayList<MarkerOptions>();
        parkLocations = new ArrayList<LatLng>();
        parkNames = new ArrayList<String>();

        parkNames.add("Mescit Parking Lot");
        parkNames.add("EE Building Parking Lot");
        parkNames.add("Unam Parking Lot");
        parkNames.add("Meteksan Parking Lot");


        parkLocations.add(new LatLng(39.867546, 32.751353));//Mescit Parking Lot 0
        parkLocations.add(new LatLng(39.872353, 32.751303));//EE Building Parking Lot 1
        parkLocations.add(new LatLng(39.869379, 32.747401));//Unam Parking Lot 2
        parkLocations.add(new LatLng(39.865868, 32.747680));//Meteksan Parking Lot 3

        //Creating marker options with the locations and names
        for(int i = 0; i < parkLocations.size(); i++)
        {
            parkMarkerOptions.add(new MarkerOptions()
                    .position(parkLocations.get(i))
                    .title(parkNames.get(i))
                    //Need to change the icon later
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_park))
            );
        }
        //Creating markers with the marker options and putting them on the map
        for(int i = 0; i < parkMarkerOptions.size(); i++)
        {
            parkMarkers.add(mMap.addMarker(parkMarkerOptions.get(i)));
        }
    }


    //Directions stuff (Hell itself, don't dead open inside)
    public static String getRequestUrl(Marker marker) {
        LatLng destination = marker.getPosition();
        String str_org = "origin=" + myCoordinates.latitude + "," + myCoordinates.longitude;
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=walking";
        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;

    }
    //The api request for getting the directions
    private static String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpsURLConnection httpsURLConnection = null;
        try
        {
            URL url = new URL(reqUrl);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.connect();

            //Response result:
            inputStream = httpsURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while( (line = bufferedReader.readLine()) != null )
            {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if( inputStream != null )
            {
                inputStream.close();
            }
            httpsURLConnection.disconnect();
        }
        return responseString;
    }
    //Inner class necessary for parsing the directions
    public static class TaskRequestDirections extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MyFragment.TaskParser taskParser = new MyFragment.TaskParser();
            taskParser.execute(s);
        }
    }
    //Inner class necessary for managing background tasks
    public static class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>>
    {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {

            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);

            ArrayList points = null;
            polylineOptions = null;
            for( List<HashMap<String, String>> path: lists )
            {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for( HashMap<String, String> point: path )
                {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));
                    points.add(new LatLng(lat,lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);

            }
            if( polylineOptions != null )
            {
                polyline = mMap.addPolyline(polylineOptions);
            }
            else
            {
                //Error in drawing the polyline, the user needs to try again or check internet connection
            }
        }
    }




}
