package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.app.Application;

import com.example.can.bilmapp.BilMappApplication;

import dagger.Module;
import dagger.Provides;
@Module
public class ApplicationModule {
    private final BilMappApplication application;

    public ApplicationModule( BilMappApplication application ) {
        this.application = application;
    }

    @Provides
    Application provideApplication()
    {
        return application;
    }

    @Provides
    BilMappApplication provideRoomApplication() {
        return application;
    }
}
