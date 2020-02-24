package com.example.can.bilmapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "usersCourses")
public class Course {
    //Properties
    @ColumnInfo
    String courseName;
    @ColumnInfo
    String courseCode;
    @ColumnInfo
    String courseInstructor;
    @ColumnInfo
    String courseSchedule;
    @PrimaryKey
    @NonNull
    String courseId;
    @Ignore
    public Course(){}

    public Course(String courseId, String courseCode, String courseName, String courseSchedule, String courseInstructor){
        this.courseCode = courseCode;
        this.courseInstructor = courseInstructor;
        this.courseName = courseName;
        this.courseSchedule = courseSchedule;
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseInstructor() {
        return courseInstructor;
    }

    public String getCourseSchedule() {
        return courseSchedule;
    }

    public String getCourseId() {
        return courseId;
    }

}