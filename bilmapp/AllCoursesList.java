package com.example.can.bilmapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AllCoursesList extends ArrayAdapter<Course> {
    //Properties of our event list
    private Activity context;
    private List<Course> courseList;

    public AllCoursesList(Activity context, List<Course> courseList){
        super(context, R.layout.all_courses_list, courseList);
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        //Create the view
        View listViewItem = inflater.inflate(R.layout.all_courses_list, null, true);

        //find the views which are in the list_layout
        TextView textViewCourseName = (TextView) listViewItem.findViewById(R.id.textViewCourseName);
        TextView textViewSchedule = (TextView) listViewItem.findViewById(R.id.textViewSchedule);

        //get the item clicked
        Course course = courseList.get(position);

        //set the values
        textViewCourseName.setText(course.getCourseName());
        textViewSchedule.setText(course.getCourseSchedule());

        //return the new list
        return listViewItem;
    }
}