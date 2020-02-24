package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.example.can.bilmapp.Event;

public class NewUserEventViewModel extends ViewModel {
    private UserEventsRepository repository;

    public  NewUserEventViewModel( UserEventsRepository repository ) {
        this.repository = repository;
    }


    public void addNewUserEvent(Event event) {
        new AddEvent().execute(event);
    }

    private class AddEvent extends AsyncTask<Event,Void,Void> {
        @Override
        protected Void doInBackground(Event... events) {
                repository.addUserEvent(events[0]);
            return null;
        }
    }
}
