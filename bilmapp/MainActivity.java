package com.example.can.bilmapp;

        import android.Manifest;
        import android.content.ContentProvider;
        import android.content.Context;
        import android.content.ContextWrapper;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.drawable.Drawable;
        import android.location.Criteria;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.provider.Settings;
        import android.support.annotation.DrawableRes;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
        import android.support.v4.app.FragmentManager;
        import android.support.v7.app.AlertDialog;
        import android.view.View;
        import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.Switch;
        import android.widget.TextView;

        import com.firebase.client.FirebaseApp;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
        import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.Scanner;

        import android.widget.Button;
        import android.widget.Toast;

/**
 * Main activity class: initializes components, asks for permissions, handles the functions of the navigation drawer and provides helper methods for other classes
 * @author Can KINIK
 * @version 05.07.2018
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    //https://material.io/tools/icons/?style=baseline    this is a very good location for icons

    //Constants
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    //Variables
    //Necessary for managing the layouts in a static manner
    public static LocationManager locationManager;

    //Fragment Variables
    SwitchFragment switchFragment;
    static CalendarFragment calendarFragment;
    static CafeMenuFragment cafeMenuFragment;
    static MyFragment myFragment;
    static UsersCoursesFragment usersCoursesFragment;
    //Needed for closing the drawer
    DrawerLayout drawerLayout;

    //Just necessary for the signature of some methods
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Added myself
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        //Floating action button is assigned with centering the user on the map
        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab);
        fab.setImageResource( R.drawable.ic_action_center );
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "User Centered", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if( MyFragment.mMap != null )
                {
                    MyFragment.centreOnMarker( MyFragment.myMarker );
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Build version and permission checks
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else requestLocation();
        if (!isLocationEnabled())
            showAlert(1);

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        InitialScreenFragment initialScreenFragment = new InitialScreenFragment();
        manager.beginTransaction().replace(R.id.mainLayout, initialScreenFragment).commit();
    }

    //For location access
    public void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = locationManager.getBestProvider(criteria, true);

        if ( ActivityCompat.checkSelfPermission( MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 1000, 1, (LocationListener) MainActivity.this);
    }
    //Checking for location enabled
    public static boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // Only want GPS but this is also possible to implement if requirement is not precise || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    //Checking for permission granted
    public boolean isPermissionGranted() {
        if ( ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED|| ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    //Alerts to show for each scenario
    private void showAlert(final int status) {
        String message, title, btnText;
        if (status == 1) {
            message = "Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                    "use this app";
            title = "Enable Location";
            btnText = "Location Settings";
        } else {
            message = "Please allow this app to access location!";
            title = "Permission access";
            btnText = "Grant";
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (status == 1) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        } else
                            requestPermissions(PERMISSIONS, PERMISSION_ALL);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }


    //If keyboard/drawer is open -> closes them, else the application is closed
    @Override
    public void onBackPressed() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    //Inflates main layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //When the settings button is clicked, map type setter is opened
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.media_route_menu_item) {
            openMapTypeSetter();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Does what is supposed to be done when a NavigationItem is selected
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.GE250_251)
        {
            openEvents(view);
        }
        else if (id == R.id.Course)
        {
            openCourses(view);
        }
        else if (id == R.id.Calendar)
        {
            openCalendar(view);
        }
        else if (id == R.id.Filters)
        {
            openFilters(view);
        }
        else if (id == R.id.FragmentTrial)
        {
            openMap(view);
        }
        else if (id == R.id.UserCourses)
        {
            openUserCoursesFragment(view);
        }
        closeDrawer();
        return true;
    }



    //Implementing the onLocationChanged from here by accessing the public static variables of the MyFragment class
    @Override
    public void onLocationChanged(Location location) {
        if( MyFragment.myCoordinates != null && MyFragment.myCoordinates != null )
        {
            MyFragment.myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
            MyFragment.myMarker.setPosition(MyFragment.myCoordinates);
        }

    }
    //Empty overrided methods required for the LocationListener interface
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
    @Override
    public void onProviderEnabled(String s) {

    }
    @Override
    public void onProviderDisabled(String s) {

    }
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    //Helper methods
    //If the drawer is open, closes it
    public void closeDrawer()
    {

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    //Starts the events activity
    public void openEvents(View view)
    {
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }
    //Starts the courses activity
    public void openCourses(View view)
    {
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
    }
    //Opens the calendar fragment
    public void openCalendar(View view)
    {
        if ( calendarFragment == null )
        {
            calendarFragment = new CalendarFragment();
        }
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mainLayout, calendarFragment).commit();
    }
    //Opens the fragment for selecting the filters to be applied to the map
    public void openFilters(View view)
    {
        if( switchFragment == null )
        {
            switchFragment = new SwitchFragment();
        }
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mainLayout, switchFragment).commit();
    }
    //Opens the map fragment
    public void openMap(View view)
    {
        if(myFragment == null)
        {
            myFragment = new MyFragment();
        }
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mainLayout, myFragment).commit();
    }
    //Opens the initial screen
    public void openInitialScreen(View view)
    {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        InitialScreenFragment initialScreenFragment = new InitialScreenFragment();
        manager.beginTransaction().replace(R.id.mainLayout, initialScreenFragment).commit();
    }
    //Opens the fragment for setting map types
    public void openMapTypeSetter()
    {
        MapTypeSetter mapTypeSetter = new MapTypeSetter();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mainLayout, mapTypeSetter).commit();
    }
    //Opens the UserCoursesFragment
    public void openUserCoursesFragment(View view)
    {
        if( usersCoursesFragment == null )
        {
            usersCoursesFragment = new UsersCoursesFragment();
        }
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mainLayout, usersCoursesFragment).commit();
    }
    //Opens the map fragment, deletes previous polylines, draws the directions for the Event building and center on it
    public static void navigateToEventBuilding(Event event, FragmentManager manager)
    {
        if( MyFragment.polyline != null )
        {
            MyFragment.polyline.remove();
        }
        Marker marker = MyFragment.getMarkerByName(event.eventBuilding );
        MyFragment.selectedMarker = marker;
        manager.beginTransaction().replace(R.id.mainLayout, myFragment).commit();
        MyFragment.drawDirectionsToMarker(marker);
        MyFragment.centreOnMarker(marker);
    }
    //Opens the map fragment, deletes previous polylines, draws the directions for the Course building and center on it
    public static void navigateToCourseBuilding(Course course, FragmentManager manager)
    {
        if( MyFragment.polyline != null )
        {
            MyFragment.polyline.remove();
        }
        Marker marker = MyFragment.getMarkerByName( getCourseBuildingName(course) );
        MyFragment.selectedMarker = marker;
        manager.beginTransaction().replace(R.id.mainLayout, myFragment).commit();
        MyFragment.drawDirectionsToMarker(marker);
        MyFragment.centreOnMarker(marker);
    }
    //Parses the course building name from the schedule
    public static String getCourseBuildingName(Course course)
    {
        String schedule = course.getCourseSchedule();
        Scanner scan = new Scanner(schedule);
        scan.next();
        scan.next();
        String temp = scan.next();
        String result = "" + temp.charAt(0);
        if( temp.charAt(1) != '-' )
        {
            result += "" + temp.charAt(1);
        }
        return result;
    }
    // Date in the form of dd-MM-yyyy
    public static String getCurrentDate()
    {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = simpleDateFormat.format(date);
        return formattedDate;
    }
    //Required for comparison
    public static int getDateMagnitude(String date)
    {
        String day = "" + date.charAt(0) + date.charAt(1);
        String month = "" + date.charAt(3) + date.charAt(4);
        String year = date.substring(6);
        int dayMag = Integer.parseInt(day);
        int mothMag = Integer.parseInt(month);
        int yearMag = Integer.parseInt(year);
        dayMag *= 1;
        mothMag *= 100;
        yearMag *= 10000;
        return dayMag + mothMag + yearMag;
    }
    //Returns true of the compared argument is outdated
    public static boolean isOutdated(String date)
    {
        return getDateMagnitude( getCurrentDate() ) > getDateMagnitude(date);
    }
    //Returns the index of course hours in the week -> Mon 8:40 = 1, Tue 9:40 = 10 etc.
    public static ArrayList<Integer> getCourseHours( Course course )
    {
        ArrayList<Integer> result = new ArrayList<>();
        String day;
        String hour;
        int dayAddition;
        String schedule = course.getCourseSchedule();
        Scanner scanner = new Scanner(schedule);
        while( scanner.hasNext() ) {
            day = scanner.next();
            if (day.equals("Mon")) {
                dayAddition = 0;
            } else if (day.equals("Tue")) {
                dayAddition = 8;
            } else if (day.equals("Wed")) {
                dayAddition = 16;
            } else if (day.equals("Thu")) {
                dayAddition = 24;
            } else {
                dayAddition = 32;
            }
            hour = scanner.next();
            int hourInt;
            if( hour.charAt(2) == ':' )
            {
                hourInt = Integer.parseInt("" + hour.charAt(1));
            }
            else
            {
                hourInt = Integer.parseInt("" + hour.charAt(0));
            }

            //10:40 and 11:40
            if (hourInt < 2) {
                hourInt += 3;
            }
            //After the break
            else if(hourInt < 8)
            {
                hourInt += 2;
            }
            //8:40 and 9:40
            else
            {
                hourInt -= 7;
            }
            result.add(hourInt + dayAddition);
            scanner.next();
        }
        return result;
    }
    public static String getCourseClassroom(Course course)
    {
        String schedule = course.getCourseSchedule();
        Scanner scan = new Scanner(schedule);
        scan.next();
        scan.next();
        return scan.next();
    }
}
/*

https://www.youtube.com/watch?v=Cy4EraxUan4  for implementing the fragment into the navigation drawer activity
https://medium.com/@nickskelton/linking-the-navigation-drawer-back-button-and-action-button-indicator-b90d148e7aab    for back button pressed
*/
