package com.example.can.bilmapp;

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

public class AddCourseActivity extends AppCompatActivity {

    //Store Events inside a list
    List<Course> courseList;

    Button buttonAddCourse;


    //decleare the database reference
    DatabaseReference databaseCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        //initialize the database
        databaseCourses = FirebaseDatabase.getInstance().getReference("courses");

        buttonAddCourse = (Button) findViewById(R.id.buttonAddEvent);

        //initialize the event list as an ArrayList
        courseList = new ArrayList<>();
        //add a listener to the button
        buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //read the database whenever the database changes
        databaseCourses.addValueEventListener(new ValueEventListener() {
            //read database changes instantly
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the arraylist for artist intially
                courseList.clear();

                //loop inside the database
                for(DataSnapshot eventSnapshot: dataSnapshot.getChildren()){
                    //get the event object
                    Course course  = eventSnapshot.getValue(Course.class);
                    //add the event into the ArrayList
                    courseList.add(course);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addCourse(){
        //read the files
        String nameInfo = "";
        String codeInfo = "";
        String instructorInfo = "";
        String scheduleInfo = "";
        try{
            //for name
            InputStream name = getAssets().open("courseName.txt");
            int sizeName = name.available();
            byte[] bufferName = new byte[sizeName];
            name.read(bufferName);
            name.close();
            nameInfo = new String(bufferName);

            //for code
            InputStream code = getAssets().open("courseCode.txt");
            int sizeCode = code.available();
            byte[] bufferCode = new byte[sizeCode];
            code.read(bufferCode);
            code.close();
            codeInfo = new String(bufferCode);

            //for instructor
            InputStream inst = getAssets().open("courseInstructor.txt");
            int sizeInst = inst.available();
            byte[] bufferInst = new byte[sizeInst];
            inst.read(bufferInst);
            inst.close();
            instructorInfo = new String(bufferInst);

            //for schedule
            InputStream sc = getAssets().open("courseSchedule.txt");
            int sizeSc = sc.available();
            byte[] bufferSc = new byte[sizeSc];
            sc.read(bufferSc);
            sc.close();
            scheduleInfo = new String(bufferSc);

        }catch (IOException ex){
            ex.printStackTrace();
        }

        Scanner scanName = new Scanner(nameInfo);
        Scanner scanCode = new Scanner(codeInfo);
        Scanner scanInstructor = new Scanner(instructorInfo);
        Scanner scanSchedule = new Scanner(scheduleInfo);

        while(scanName.hasNext()) {
            String courseName = scanName.nextLine();
            String courseCode = scanCode.nextLine();
            String courseInstructor = scanInstructor.nextLine();
            String courseSchedule = scanSchedule.nextLine();
            //Creating an unique string
            String id = databaseCourses.push().getKey();

            //Create a new event
            Course course = new Course(id, courseCode, courseName, courseSchedule, courseInstructor);

            //display if action is succsessful
            Toast.makeText(this, "Course added", Toast.LENGTH_LONG).show();

            //Store the event inside the database, use id to store event as a child
            databaseCourses.child(id).setValue(course);
        }

    }

}
