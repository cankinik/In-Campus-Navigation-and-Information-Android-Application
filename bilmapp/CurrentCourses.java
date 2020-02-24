package com.example.can.bilmapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
//https://www.youtube.com/watch?v=nQnyAXJxngY   for searchable listview
public class CurrentCourses extends AppCompatActivity {

    //listView for your event class
    ListView listViewCourses;

    //Store Events inside a list
    List<Course> courseList;

    //decleare the database reference
    DatabaseReference databaseCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_courses);

        //initialize the database
        databaseCourses = FirebaseDatabase.getInstance().getReference("courses");

        //find your listView
        listViewCourses = (ListView) findViewById(R.id.listViewCourses);

        //initialize the event list as an ArrayList
        courseList = new ArrayList<>();

        //make your events clickible and display a message
        //ArrayAdapter<Event> getInfoAdapter = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, eventList);
        //listViewEvents.setAdapter(getInfoAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //read the database whenever the database changes
        databaseCourses.addValueEventListener(new ValueEventListener() {
            //read database changes instantly
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the arraylist for artist intially
                courseList.clear();

                //loop inside the database
                for(DataSnapshot eventSnapshot: dataSnapshot.getChildren()){
                    //get the event object
                    Course course  = eventSnapshot.getValue(Course.class);
                    //add the event into the ArrayList
                    courseList.add(course);
                }

                //Adapter responsible of the view
                AllCoursesList adapter = new AllCoursesList(CurrentCourses.this, courseList);
                listViewCourses.setAdapter(adapter);
                listViewCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        final AlertDialog.Builder optionsBuilder = new AlertDialog.Builder(view.getContext());

                        optionsBuilder.setTitle("Choose an option for: " + courseList.get(position).getCourseName() );
                        final AlertDialog optionsDialog = optionsBuilder.create();
                        optionsDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"Navigate To Building", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if( MainActivity.myFragment == null )
                                {
                                    MainActivity.myFragment = new MyFragment();
                                }
                                android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
                                manager.beginTransaction().replace(R.id.mainLayout, MainActivity.myFragment);
                                MainActivity.navigateToCourseBuilding(courseList.get(position), getSupportFragmentManager());
                                finish();
                            }
                        });
                        optionsDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Add To Schedule", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.usersCoursesFragment.coursesToBeAdded.add( courseList.get( position ) );
                            }
                        });
                        optionsDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        optionsDialog.show();
//                        Toast.makeText(CurrentCourses.this, "Directions drawn to: " + MainActivity.getCourseBuildingName(courseList.get(position)) + MainActivity.getCourseHours(courseList.get(position)), Toast.LENGTH_SHORT).show();
//                        MainActivity.navigateToCourseBuilding(courseList.get(position), getSupportFragmentManager());
//                        finish();

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}