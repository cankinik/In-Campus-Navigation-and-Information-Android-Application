package com.example.can.bilmapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {
    //define your event components
    EditText editTextName;
    EditText editTextDate;
    EditText editTextClub;
    Spinner spinnerBuilding;
    Button buttonAddEvent;


    //decleare the database reference
    DatabaseReference databaseEvents;

    //listView for your event class
    //ListView listViewEvents;

    //Store Events inside a list
    List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //initialize the database
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

        //find your components from XML file
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        spinnerBuilding = (Spinner) findViewById(R.id.spinnerBuilding);
        buttonAddEvent = (Button) findViewById(R.id.buttonAddEvent);
        editTextClub = (EditText) findViewById(R.id.editTextClub);

        //find your listView
        //listViewEvents = (ListView) findViewById(R.id.listViewEvents);

        //initialize the event list as an ArrayList
        eventList = new ArrayList<>();

        //add a listener to the button
        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
                //Checks the eventList for events that have expired and deletes those who are outdated.
                deleteEvent();
            }
        });
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
                //EventList adapter = new EventList(EventActivity.this, eventList);
                //listViewEvents.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* Add event when button clicked
     */
    private void addEvent(){
        String eventName = editTextName.getText().toString().trim();
        String eventDate = editTextDate.getText().toString().trim();
        String eventBuilding = spinnerBuilding.getSelectedItem().toString();
        String eventClub = editTextClub.getText().toString().trim();

        //make sure admin type the event name
        if(!TextUtils.isEmpty(eventName)){

            //Using the helper method from the MainActivity to check if the date is appropriate
            if( !MainActivity.isOutdated(eventDate) )
            {
                //Creating an unique string
                String id = databaseEvents.push().getKey();

                //Create a new event
                Event event = new Event(id, eventName, eventDate, eventBuilding, eventClub);



                //display if action is succsessful
                Toast.makeText(this, "Event added", Toast.LENGTH_LONG).show();

                //Store the event inside the database, use id to store event as a child
                databaseEvents.child(id).setValue(event);
            }
            else
            {
                //error message for wrong date
                Toast.makeText(this, "The entered date is in the past!!!...", Toast.LENGTH_LONG).show();
            }

        }
        else{
            Toast.makeText(this, "You have to enter an event name...", Toast.LENGTH_LONG).show();
        }

        //you can also make sure string typed with the correct format below
    }
    //Method for deleting an event
    private void deleteEvent()
    {
        for(int i = 0; i < eventList.size(); i++)
        {
            if( MainActivity.isOutdated( eventList.get(i).getEventDate() ) )
            {
                DatabaseReference pastDatabase;
                String eventId = eventList.get(i).getEventId();
                pastDatabase = FirebaseDatabase.getInstance().getReference("events").child(eventId);
                databaseEvents.removeValue();
            }
        }
    }
}