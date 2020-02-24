package com.example.can.bilmapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static dagger.android.AndroidInjection.inject;

public class CurrentEvents extends AppCompatActivity {

    //listView for your event class
    ListView listViewEvents;


    //Store Events inside a list
    List<Event> eventList;

    //decleare the database reference
    DatabaseReference databaseEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.current_events);

        //initialize the database
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

        //find your listView
        listViewEvents = (ListView) findViewById(R.id.listViewEvents);

        //initialize the event list as an ArrayList
        eventList = new ArrayList<>();

        //make your events clickible and display a message
        //ArrayAdapter<Event> getInfoAdapter = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, eventList);
        //listViewEvents.setAdapter(getInfoAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //read the database whenever the database changes
        databaseEvents.addValueEventListener(new ValueEventListener() {
            //read database changes instantly
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the arraylist for artist intially
                eventList.clear();

                //loop inside the database
                for(DataSnapshot eventSnapshot: dataSnapshot.getChildren()){
                    //get the event object
                    Event event  = eventSnapshot.getValue(Event.class);
                    //add the event into the ArrayList
                    eventList.add(event);
                }

                //Adapter responsible of the view
                AllEventsList adapter = new AllEventsList(CurrentEvents.this, eventList);
                listViewEvents.setAdapter(adapter);
                listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Event selectedEvent = eventList.get(position);

                        final AlertDialog.Builder optionsBuilder = new AlertDialog.Builder(view.getContext());

                        optionsBuilder.setTitle("Choose an option for: " + eventList.get(position).getEventName() );
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
                                MainActivity.navigateToEventBuilding(eventList.get(position), getSupportFragmentManager());
                                finish();
                            }
                        });
                        optionsDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Add To Calendar", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(MainActivity.calendarFragment == null)
                                {
                                    MainActivity.calendarFragment = new CalendarFragment();
                                }
                                android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
                                MainActivity.calendarFragment.selectedEvent = eventList.get(position);
                                manager.beginTransaction().replace(R.id.mainLayout, MainActivity.calendarFragment).commit();
                                finish();
                            }
                        });
                        optionsDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        optionsDialog.show();


                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}