/**
 * Uses a modified version of the CalendarView called Caldroid to highlight event dates, which, upon clicking, display options
 * @author Mert Ertuğrul, Group G5I , Section 5
 * @version 05.05.2018
 */

package com.example.can.bilmapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import java.util.List;
import android.graphics.drawable.Drawable;

import com.example.can.bilmapp.Room.UsersEvents.viewmodel.NewUserEventViewModel;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.UserEventsCollectionViewModel;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.UserEventsViewModel;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.CaldroidFragment;

import android.view.ViewGroup;
import android.widget.Button;
import java.util.ArrayList;

import javax.inject.Inject;

public class CalendarFragment extends Fragment {

    //properties
    public Event selectedEvent;
    final SimpleDateFormat eventFormat;
    List<Event> events;
    CaldroidFragment myCaldroid;
    ArrayList<Event> eventsToday;
    ArrayList<String> eventInfo;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    UserEventsCollectionViewModel userEventsCollectionViewModel;
    UserEventsViewModel userEventsViewModel;
    NewUserEventViewModel newUserEventViewModel;


    //constructor
    public CalendarFragment() {
        eventFormat = new SimpleDateFormat("dd-MM-yyyy");
        eventsToday = new ArrayList<Event>();
        eventInfo = new ArrayList<>();
        events = new ArrayList<>();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        Button editRemindersButton = rootView.findViewById( R.id.reminderButton);
        editRemindersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().replace( R.id.mainLayout, new TempFragment()).commit();
            }
        });
        //setting up the caldroid
        createCaldroid();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userEventsCollectionViewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                reloadCalendarEvents(events);
                createCaldroid();
            }
        });
        Event dummy = new Event("1a","Coding Course","06-05-2018", "B", "IEEE");
        newUserEventViewModel.addNewUserEvent(dummy);
        userEventsCollectionViewModel.deleteEvent(dummy);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BilMappApplication) getActivity().getApplication())
                .getapplicationComponent()
                .inject(this);
        userEventsCollectionViewModel = ViewModelProviders.of(this,viewModelFactory).get(UserEventsCollectionViewModel.class);
        userEventsViewModel = ViewModelProviders.of(this,viewModelFactory).get(UserEventsViewModel.class);
        newUserEventViewModel = ViewModelProviders.of(this,viewModelFactory).get(NewUserEventViewModel.class);
        if( selectedEvent != null )
        {
            newUserEventViewModel.addNewUserEvent(selectedEvent);
            reloadCalendarEvents(events);
            createCaldroid();
        }
    }
    @Override
    public void onResume()
    {
        if( selectedEvent != null )
        {
            newUserEventViewModel.addNewUserEvent(selectedEvent);
            reloadCalendarEvents(events);
            createCaldroid();
        }
        super.onResume();

    }

    private void reloadCalendarEvents(List<Event> newEvents){
        this.events = newEvents;

    }

    private void createCaldroid(){
        myCaldroid = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        //highlighted the dates that have reminders
        try {
            highlightEvents();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        myCaldroid.setArguments(args);

        //Attach to eventCalendar
        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.eventCalendar, myCaldroid);
        t.commit();

        //Listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                for (Event element : events ) {
                    try {
                        if ( eventFormat.parse( element.getEventDate()).equals(date) ) {
                            eventsToday.add( element );
                            eventInfo.add(TempFragment.eventInfoConverter( element ));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (eventsToday.size() > 0 && eventFormat.parse( eventsToday.get(0).getEventDate() ).equals(date)) {

                        CustomDialogue dialogue = new CustomDialogue(eventsToday, eventInfo);
                        dialogue.CalendarClicked(view);
                        eventsToday = new ArrayList<>();
                        eventInfo = new ArrayList<>();

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        myCaldroid.setCaldroidListener(listener);
    }
    private void removeColor(View lastSelected){
        if ( lastSelected != null) {
            lastSelected.setBackgroundResource(android.R.color.transparent);
        }
    }



    /**highlights the dates on the calender that have reminders (selected events)
     */
    private void highlightEvents() throws ParseException {
        // methods written here are symbolic and will change
        Date eventDate;
        Drawable eventSymbol = getActivity().getDrawable(R.drawable.eventicon);
        Log.i("highlighter called", "trial high");
        for (Event element : events) {
            Log.i(TempFragment.eventInfoConverter(element), "inside highlighter");
            eventDate = eventFormat.parse( element.getEventDate() );

            myCaldroid.setBackgroundDrawableForDate(eventSymbol, eventDate);

        }
    }

}
