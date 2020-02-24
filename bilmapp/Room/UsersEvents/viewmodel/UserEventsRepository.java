package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.arch.lifecycle.LiveData;

import com.example.can.bilmapp.Course;
import com.example.can.bilmapp.Event;

import java.util.List;

import javax.inject.Inject;

public class UserEventsRepository {
    private final UserEventsAccessor userEventsAccessor;
    private final UserCoursesAccessor userCoursesAccessor;

    @Inject
    public UserEventsRepository(UserEventsAccessor userEventsAccessor, UserCoursesAccessor userCoursesAccessor ) {
        this.userEventsAccessor = userEventsAccessor;
        this.userCoursesAccessor = userCoursesAccessor;
    }

    public LiveData<List<Event>> getEventList(){
        return userEventsAccessor.loadAllUserEvents();
    }
    public LiveData<Event> getEventById(String id){
        return userEventsAccessor.getEventById(id);
    }
    public LiveData<List<Course>> getCourseList(){
        return userCoursesAccessor.loadAllUserCourses();
    }
    public LiveData<Course> getCourseById(String id){
        return userCoursesAccessor.getCourseById(id);
    }

    public void deleteEvent(Event event){
        userEventsAccessor.deleteEvent(event);
    }
    public void updateEvent(Event event){
        userEventsAccessor.updateEvent(event);
    }
    public void addUserEvent(Event event){
        userEventsAccessor.addUserEvent(event);
    }
    public void deleteCourse(Course course){
        userCoursesAccessor.deleteCourse(course);
    }
    public void updateCourse(Course course){
        userCoursesAccessor.updateCourse(course);
    }
    public void addUserCourse(Course course){
        userCoursesAccessor.addUserCourse(course);
    }

}
