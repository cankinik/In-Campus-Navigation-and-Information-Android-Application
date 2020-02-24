package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.example.can.bilmapp.Course;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.UserEventsRepository;

public class NewUserCourseViewModel extends ViewModel {
    private UserEventsRepository repository;

    public  NewUserCourseViewModel( UserEventsRepository repository ) {
        this.repository = repository;
    }


    public void addNewUserCourse(Course course) {
        new AddCourse().execute(course);
    }

    private class AddCourse extends AsyncTask<Course,Void,Void> {
        @Override
        protected Void doInBackground(Course... courses) {
            repository.addUserCourse(courses[0]);
            return null;
        }
    }
}