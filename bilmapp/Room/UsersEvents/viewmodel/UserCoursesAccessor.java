package com.example.can.bilmapp.Room.UsersEvents.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.can.bilmapp.Course;
import com.example.can.bilmapp.Event;

import java.util.List;

@Dao
public interface UserCoursesAccessor {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUserCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("SELECT * FROM USERSCOURSES")
    LiveData<List<Course>> loadAllUserCourses();

    @Query("SELECT * FROM USERSCOURSES WHERE courseId = :id")
    LiveData<Course> getCourseById(String id);
}