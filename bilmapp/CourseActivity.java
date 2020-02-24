package com.example.can.bilmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CourseActivity extends AppCompatActivity {

    //Properties
    private Button courseButton;
    private Button allCoursesButton;
    private Button userCoursesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        courseButton = (Button) findViewById(R.id.courseButton);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPasswordActivity();
            }
        });

        allCoursesButton = (Button) findViewById(R.id.allCoursesButton);
        allCoursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllCourses();
            }
        });

        userCoursesButton = (Button) findViewById(R.id.userCoursesButton);
        userCoursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUsersCourses();
            }
        });
    }


    public void openPasswordActivity(){
        Intent intent = new Intent(this, CoursePassword.class);
        startActivity(intent);
        finish();
    }
    /* Go to the current Ge250 events page...
     */
    public void showAllCourses(){
        Intent intent = new Intent(this, CurrentCourses.class);
        startActivity(intent);
        finish();
    }
    public void showUsersCourses()
    {
        if( MainActivity.usersCoursesFragment == null )
        {
            MainActivity.usersCoursesFragment = new UsersCoursesFragment();
        }
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mainLayout, MainActivity.usersCoursesFragment).commit();
        finish();
    }

}
