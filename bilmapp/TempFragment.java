package com.example.can.bilmapp;

import android.app.Notification;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.DateFormat;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.can.bilmapp.Room.UsersEvents.viewmodel.NewUserEventViewModel;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.UserEventsCollectionViewModel;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.UserEventsViewModel;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;


public class TempFragment extends Fragment {

    //properties
    private static ArrayList<String> eventStrings;
    private static List<Event> events;
    private static ListView eventEditList;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    UserEventsCollectionViewModel userEventsCollectionViewModel;
    UserEventsViewModel userEventsViewModel;
    NewUserEventViewModel newUserEventViewModel;


    //constructor

    public TempFragment() {

        eventStrings = new ArrayList<String>();
        events = new ArrayList<Event>();
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userEventsCollectionViewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {

                reloadFragment(events);
            }
        });
        Event dummy = new Event("1a","Coding Course","06-05-2018", "B", "IEEE");
        newUserEventViewModel.addNewUserEvent(dummy);
        userEventsCollectionViewModel.deleteEvent(dummy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_reminders, container, false);
        generateEventInfo();

        eventEditList = rootView.findViewById(R.id.eventEditList);
        eventEditList.setAdapter(new CustomListAdapter(getActivity().getApplicationContext(), R.layout.event_list_item, eventStrings));
        return rootView;
    }

    private void reloadFragment( List<Event> newEvents) {
        this.events = newEvents;
        generateEventInfo();
        eventEditList.setAdapter(new CustomListAdapter(getActivity().getApplicationContext(), R.layout.event_list_item, eventStrings));
    }
    protected static void generateEventInfo() {
        //event information stored as string in eventStrings to be show in the list
        eventStrings = new ArrayList<String>();
        if (events.size() == 0) {
            eventStrings.add("No events added as of yet.");
        } else {
            for (Event element : events) {
                eventStrings.add(eventInfoConverter(element));
            }
        }

    }
    private class CustomListAdapter extends ArrayAdapter<String> {
        private int layout;
        //sets up the adapter to modify the listview as needed
        private CustomListAdapter(Context context, int resource,List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainHolder = null;
            if ( convertView == null ) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.editButton =  convertView.findViewById(R.id.editEventButton);
                viewHolder.eventText = (TextView) convertView.findViewById(R.id.eventText);
                convertView.setTag(viewHolder);
            }
            mainHolder = (ViewHolder) convertView.getTag();
            mainHolder.eventText.setText(eventStrings.get(position));
            mainHolder.editButton.setImageResource(R.drawable.editsmall);
            mainHolder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( events.size() > 0) {
                        createEventPopUp(position);
                    }
                }
            });
            return convertView;
        }
    }
    public class ViewHolder {
        TextView eventText;
        ImageButton editButton;
    }

    /**
     * Adds all the info about an event to a string
     * @param convertedEvent the event whose information will be converted
     * @return result the final string
     */
    public static String eventInfoConverter( Event convertedEvent ) {
        String result = "";
        result += "Title: " + convertedEvent.getEventName() + "\n";
        result += "Date: " + convertedEvent.getEventDate() + " Time: " + convertedEvent.getEventDate() + "\n";
        result += "Building: " + convertedEvent.getEventBuilding() + "\n";
        result += "Club: " + convertedEvent.getEventClub() + "\n";
        return result;
    }
    //
    public void createEventPopUp( int position ) {
        final AlertDialog.Builder optionsBuilder = new AlertDialog.Builder(this.getView().getContext());
        final Event selectedEvent = events.get(position);
        optionsBuilder.setTitle("Choose an option for: " + selectedEvent.getEventName() );
        final AlertDialog optionsDialog = optionsBuilder.create();
        optionsDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Delete Event", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userEventsCollectionViewModel.deleteEvent(selectedEvent);
                reloadFragment(events);
                optionsDialog.dismiss();
            }
        });

        optionsDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Add Notification", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SimpleDateFormat morningEventFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                Date when = null;
                try {
                    when = morningEventFormat.parse(selectedEvent.getEventDate() + " 08:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if ( when != null) {
                    NotificationCompat.Builder eventNotificationBuilder = new NotificationCompat.Builder(getContext(), "event_notification_channel")
                            .setSmallIcon(R.drawable.bilkent_logo)
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                            .setContentTitle("Hey there! Here's a quick reminder im case you've forgotten an event.")
                            .setContentText(eventInfoConverter(selectedEvent))
                            .setWhen(when.getTime())
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                    notificationManager.notify(((int) when.getTime()), eventNotificationBuilder.build());
                    Toast.makeText(getContext(),"Notification created", Toast.LENGTH_LONG);
                }

            }
        });
        optionsDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("cancel", "1");
            }
        });
        optionsDialog.show();
    }

}