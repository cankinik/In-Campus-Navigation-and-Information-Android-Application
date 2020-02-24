package com.example.can.bilmapp.Room.UsersEvents.viewmodel;


import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.example.can.bilmapp.Room.UsersEvents.viewmodel.CustomViewModelFactory;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.UserEventsAccessor;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.UserEventsRepository;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.UsersEventsDatabase;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private final UsersEventsDatabase database;

    public DatabaseModule(Application application) {
        this.database = Room.databaseBuilder(application, UsersEventsDatabase.class , "UserEvents.db").fallbackToDestructiveMigration().build();
    }
    @Provides
    @Singleton
    UserEventsRepository provideUserEventsRepository(UserEventsAccessor userEventsAccessor, UserCoursesAccessor userCoursesAccessor){
        return new UserEventsRepository(userEventsAccessor, userCoursesAccessor);
    }

    @Provides
    @Singleton
    UserEventsAccessor provideUserAccessor(UsersEventsDatabase usersEventsDatabase){
        return database.userEventsAccessor();
    }
    @Provides
    @Singleton
    UserCoursesAccessor provideUserCoursesAccessor(UsersEventsDatabase usersEventDatabase){
        return database.userCoursesAccessor();
    }

    @Provides
    @Singleton
    UsersEventsDatabase provideUserDatabase(Application application){
        return database;
    }
    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(UserEventsRepository userEventsRepository ){
        return new CustomViewModelFactory(userEventsRepository);
    }
}
