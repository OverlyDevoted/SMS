package com.example.programavimotechnologijos;

import com.example.control.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SubjectsInCourses {
    int id;
    int course_id;
    int subject_id;

    public SubjectsInCourses() {
    }

    public SubjectsInCourses(int id, int course_id, int subject_id) {
        this.id = id;
        this.course_id = course_id;
        this.subject_id = subject_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }
    public static void createRelation(int subject_id, int course_id) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        String insert = "INSERT INTO subjects_in_courses(subject_id, course_id) VALUES (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1, subject_id);
        preparedStatement.setInt(2, course_id);
        preparedStatement.execute();
        DbUtils.disconnectFromDb(connection, preparedStatement);
    }
    public static void deleteRelation(int subject_id, int course_id) throws SQLException, ClassNotFoundException {
        DbUtils.tryQuery(new String("DELETE FROM subjects_in_courses WHERE subject_id = "+ subject_id + " and course_id = " + course_id));
    }


}
