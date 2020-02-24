package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.app.Application;
import android.view.View;

import com.example.can.bilmapp.CalendarFragment;
import com.example.can.bilmapp.TempFragment;
import com.example.can.bilmapp.UsersCoursesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, DatabaseModule.class})
public interface ApplicationComponent {
    void inject(CalendarFragment fragment);
    void inject(TempFragment fragment);
    void inject(UsersCoursesFragment fragment);
    Application application();
}
