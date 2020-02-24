package com.example.can.bilmapp;

import android.app.Activity;
import android.app.Notification;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomDialogue extends AppCompatActivity {
    ArrayList<Event> eventList;
    ArrayList<String> listInfo;

    public CustomDialogue(ArrayList<Event> eventList, ArrayList<String> listInfo){
        this.eventList = eventList;
        this.listInfo = listInfo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void CalendarClicked(View view ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        View dialogueLayout = layoutInflater.inflate(R.layout.calendar_dialogue, null);
        final ListView events = dialogueLayout.findViewById(R.id.todays_events_list);
        final TextView prompt = dialogueLayout.findViewById(R.id.dialogueText);
        events.setAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.simple_list_item, listInfo));
        events.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        events.setSelector(R.color.common_google_signin_btn_text_light_focused);
        events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long l) {
                events.setSelection(position);
                final Event selectedItem = eventList.get(position);
                final AlertDialog.Builder optionsBuilder = new AlertDialog.Builder(view.getContext());

                optionsBuilder.setTitle("Choose an option for: " + selectedItem.getEventName() );
                final AlertDialog optionsDialog = optionsBuilder.create();
                optionsDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"Navigate To Building", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.navigateToEventBuilding(eventList.get(events.getSelectedItemPosition()), getSupportFragmentManager());
                        finish();
                    }
                });
                optionsDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Set Reminder", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SimpleDateFormat morningEventFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                        Date when = null;
                        try {
                            when = morningEventFormat.parse(selectedItem.getEventDate() + " 08:00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if ( when != null) {
                            NotificationCompat.Builder eventNotificationBuilder = new NotificationCompat.Builder(view.getContext(), "event_notification")
                                    .setSmallIcon(R.drawable.bilkent_logo)
                                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                    .setContentTitle("Hey there! Here's a quick reminder im case you've forgotten an event.")
                                    .setContentText(TempFragment.eventInfoConverter(selectedItem))
                                    .setWhen(when.getTime())
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(view.getContext());
                            notificationManager.notify(((int) when.getTime()), eventNotificationBuilder.build());
                        }
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

        builder.setTitle("Selected Events of the Day");
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setView(dialogueLayout);
        builder.create().show();
        
    }
}
