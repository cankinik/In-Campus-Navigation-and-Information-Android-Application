package com.example.can.bilmapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.can.bilmapp.Room.UsersEvents.viewmodel.CustomViewModelFactory;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.NewUserCourseViewModel;
import com.example.can.bilmapp.Room.UsersEvents.viewmodel.UserCoursesCollectionViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class UsersCoursesFragment extends Fragment {

    //properties
    List<Course> loadedCourses;
    ArrayList<Course> coursesToBeAdded = new ArrayList<>();

    @Inject
    CustomViewModelFactory customViewModelFactory;
    UserCoursesCollectionViewModel userCoursesCollectionViewModel;
    NewUserCourseViewModel newUserCourseViewModel;

      public UsersCoursesFragment() {
        loadedCourses = new ArrayList<>();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if( coursesToBeAdded.size() != 0 )
        {
            for( Course course : coursesToBeAdded )
            {
                newUserCourseViewModel.addNewUserCourse(course);
            }
        }

        userCoursesCollectionViewModel.getCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
                loadedCourses = courses;
            }
        });
        Course dummy = new Course("1a","cs102","CS102", "Tue 10:40-11:30 B-Z08[S]", "Aynur Dayanık");
        newUserCourseViewModel.addNewUserCourse(dummy);
        userCoursesCollectionViewModel.deleteCourse(dummy);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BilMappApplication) getActivity().getApplication())
                .getapplicationComponent()
                .inject(this);
        userCoursesCollectionViewModel = ViewModelProviders.of(this,customViewModelFactory).get(UserCoursesCollectionViewModel.class);
        newUserCourseViewModel = ViewModelProviders.of(this,customViewModelFactory).get(NewUserCourseViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          View rootView = inflater.inflate(R.layout.fragment_users_courses, container, false);
        for ( Course element : loadedCourses) {
            ArrayList<Integer> courseHours = MainActivity.getCourseHours(element);
            for (int hour : courseHours) {
                String id = "a" + hour;
                int cellID = getResources().getIdentifier(id, "id",getActivity().getPackageName() );
                ((TextView)rootView.findViewById(cellID)).setText(convertToInfo(element));
            }
        }

        return rootView;
    }
    private String convertToInfo(Course course) {
          String info = "";
          info += course.getCourseCode() + "\n" ;
          info += MainActivity.getCourseClassroom(course);
          return info;
    }
    public void addUserCourse(Course course)
    {
        newUserCourseViewModel.addNewUserCourse(course);
    }
}
