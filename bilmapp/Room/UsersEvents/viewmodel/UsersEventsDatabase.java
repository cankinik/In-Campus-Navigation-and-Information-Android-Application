package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.can.bilmapp.Course;
import com.example.can.bilmapp.Event;

@Database(entities = {Event.class, Course.class},version = 3, exportSchema = false)
public abstract class UsersEventsDatabase extends RoomDatabase{
    public abstract UserEventsAccessor userEventsAccessor();
    public abstract UserCoursesAccessor userCoursesAccessor();
}
