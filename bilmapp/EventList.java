package com.example.can.bilmapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EventList extends ArrayAdapter<Event> {
    //Properties of our event list
    private Activity context;
    private List<Event> eventList;

    public EventList(Activity context, List<Event> eventList){
        super(context, R.layout.list_layout, eventList);
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        //Create the view
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        //find the views which are in the list_layout
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewDate);

        //get the item clicked
        Event event = eventList.get(position);

        //set the values
        textViewName.setText(event.getEventName());
        textViewDate.setText(event.getEventDate());

        //return the new list
        return listViewItem;
    }
}