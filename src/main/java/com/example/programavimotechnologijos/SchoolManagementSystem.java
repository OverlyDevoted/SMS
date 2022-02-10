package com.example.programavimotechnologijos;

import com.example.control.DbUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SchoolManagementSystem implements Serializable{
    public static final String saveFile = "src/SchoolManagementSystem.txt";
    private static int currentUser;

    public SchoolManagementSystem() throws IOException, ClassNotFoundException {

    }


    public static boolean checkStudentsLogin(int id, String password) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT id, password FROM students WHERE id = '" + id + "' AND password = '" + password + "'";
        ResultSet rs = statement.executeQuery(query);
        boolean result=false;
        while(rs.next())
        {
            result = true;
        }
        DbUtils.disconnectFromDb(connection,statement);
        return result;
    }
    public static boolean checkTeachersLogin(int id, String password) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT id, password FROM teachers WHERE id = '" + id + "' AND password = '" + password + "'";
        ResultSet rs = statement.executeQuery(query);
        boolean result=false;
        while(rs.next())
        {
            result = true;
        }
        DbUtils.disconnectFromDb(connection,statement);
        return result;
    }

    public static int getNumberOfTeachers() throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        //String query = "SELECT * FROM students";
        String query = "SELECT * FROM teachers";
        ResultSet rs = statement.executeQuery(query);
        int count =0;
        while(rs.next()){
            count++;
        }
        DbUtils.disconnectFromDb(connection, statement);
        return count;
    }
    public static int getNumberOfStudents() throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        //String query = "SELECT * FROM students";
        String query = "SELECT * FROM students";
        ResultSet rs = statement.executeQuery(query);
        int count =0;
        while(rs.next()){
            count++;
        }
        DbUtils.disconnectFromDb(connection, statement);
        return count;
    }

    public static int getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(int currentUser) {
        SchoolManagementSystem.currentUser = currentUser;
    }
}
