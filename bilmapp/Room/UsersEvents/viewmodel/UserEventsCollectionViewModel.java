package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.example.can.bilmapp.Event;

import java.util.List;

public class UserEventsCollectionViewModel extends ViewModel{
    private UserEventsRepository repository;

    public UserEventsCollectionViewModel(UserEventsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Event>> getEvents() {
        return repository.getEventList();
    }

    public void deleteEvent(Event event) {
        DeleteItem deleteItem = new DeleteItem();
        deleteItem.execute(event);
    }
    private class DeleteItem extends AsyncTask<Event, Void, Void > {
        @Override
        protected Void doInBackground(Event... events) {
            repository.deleteEvent(events[0]);
            return null;
        }
    }
}
