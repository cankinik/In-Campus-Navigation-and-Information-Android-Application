package com.example.can.bilmapp;

import android.app.Application;

import com.example.can.bilmapp.Room.UsersEvents.viewmodel.ApplicationComponent;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.ApplicationModule;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.DaggerApplicationComponent;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.DatabaseModule;

public class BilMappApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate(){
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this))
                .databaseModule(new DatabaseModule(this))
                .build();
    }

    public ApplicationComponent getapplicationComponent() {
        return applicationComponent;
    }
}
