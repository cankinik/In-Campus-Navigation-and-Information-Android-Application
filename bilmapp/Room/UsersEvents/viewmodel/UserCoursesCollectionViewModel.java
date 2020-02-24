package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.example.can.bilmapp.Course;
import com.example.can.bilmapp.Event;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.UserEventsRepository;

import java.util.List;

public class UserCoursesCollectionViewModel extends ViewModel {
    private UserEventsRepository repository;

    public UserCoursesCollectionViewModel(UserEventsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Course>> getCourses() {
        return repository.getCourseList();
    }

    public void deleteCourse(Course course) {
        DeleteItem deleteItem = new DeleteItem();
        deleteItem.execute(course);
    }
    private class DeleteItem extends AsyncTask<Course, Void, Void > {
        @Override
        protected Void doInBackground(Course... course) {
            repository.deleteCourse(course[0]);
            return null;
        }
    }
}