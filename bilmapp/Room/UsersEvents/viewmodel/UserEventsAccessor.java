package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.can.bilmapp.Event;

import java.util.List;

@Dao
public interface UserEventsAccessor {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUserEvent(Event event);

    @Update
    void updateEvent(Event event);

    @Delete
    void deleteEvent(Event event);

    @Query("SELECT * FROM USERSEVENTS")
    LiveData<List<Event>> loadAllUserEvents();

    @Query("SELECT * FROM USERSEVENTS WHERE eventId = :id")
    LiveData<Event> getEventById(String id);
}
