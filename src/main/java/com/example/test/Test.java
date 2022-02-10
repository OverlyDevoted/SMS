package com.example.test;

import com.example.control.DbUtils;
import com.example.programavimotechnologijos.Course;
import com.example.programavimotechnologijos.Group;
import com.example.programavimotechnologijos.Teacher;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println(DbUtils.tryGetting("SELECT * FROM COURSE"));
        System.out.println(Course.getCourseByID(14).toString());
        System.out.println(Teacher.getTeacherById(2019001).toString());
        System.out.println(Group.getGroupByID(12).toString());

    }
}
