package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.can.bilmapp.Event;

public class UserEventsViewModel extends ViewModel{
    private UserEventsRepository userEventsRepository;

    public UserEventsViewModel(UserEventsRepository repository) {
        this.userEventsRepository = repository;
    }

    public LiveData<Event> getEventById(String id) {
        return  userEventsRepository.getEventById(id);
    }
}
