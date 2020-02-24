package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class CustomViewModelFactory implements ViewModelProvider.Factory {

    private final UserEventsRepository repository;
    @Inject
    public CustomViewModelFactory( UserEventsRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserEventsViewModel.class)) {
            return (T) new UserEventsViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(UserEventsCollectionViewModel.class)) {
            return (T) new UserEventsCollectionViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(NewUserEventViewModel.class)) {
            return (T) new NewUserEventViewModel(repository);
        }
        if (modelClass.isAssignableFrom(UserCoursesViewModel.class)) {
            return (T) new UserCoursesViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(UserCoursesCollectionViewModel.class)) {
            return (T) new UserCoursesCollectionViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(NewUserCourseViewModel.class)) {
            return (T) new NewUserCourseViewModel(repository);
        }
        else {
            throw new IllegalArgumentException("Viewmodel not found");
        }

    }
}
