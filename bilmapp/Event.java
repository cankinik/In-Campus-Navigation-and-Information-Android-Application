package com.example.can.bilmapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "usersEvents")
public class Event {

    //Properties
    @PrimaryKey
    @NonNull
    String eventId;
    @ColumnInfo
    String eventName;
    @ColumnInfo
    String eventDate;
    @ColumnInfo
    String eventBuilding;
    @ColumnInfo
    String eventClub;
    @ColumnInfo
    int dayReminder;
    //Empty constructor used while reading the values
    @Ignore
    public Event(){}

    //Constructor
    public Event(String eventId, String eventName, String eventDate, String eventBuilding, String eventClub){
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventBuilding = eventBuilding;
        this.eventClub = eventClub;
        dayReminder = 0;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventBuilding() {
        return eventBuilding;
    }

    public String getEventClub() {
        return eventClub;
    }

    public void setDayReminder(int condition)
    {
        dayReminder = condition;
    }
    public int getDayReminder()
    {
        return dayReminder;
    }
}
