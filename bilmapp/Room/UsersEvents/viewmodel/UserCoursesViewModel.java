package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;


import com.example.can.bilmapp.Course;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.UserEventsRepository;

public class UserCoursesViewModel extends ViewModel {
    private UserEventsRepository userEventsRepository;

    public UserCoursesViewModel(UserEventsRepository repository) {
        this.userEventsRepository = repository;
    }

    public LiveData<Course> getCourseById(String id) {
        return  userEventsRepository.getCourseById(id);
    }
}
