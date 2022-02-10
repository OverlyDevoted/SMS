package com.example.control;

import com.example.programavimotechnologijos.Course;

import java.sql.*;

public class DbUtils {
    public static Connection connectToDb() throws ClassNotFoundException {
        Connection conn = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        String DB_URL = "jdbc:mysql://localhost:3306/schoolmanagementdb";
        String USER = "root";
        String PASS = "admin";
        try{
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return conn;
    }

    public static void tryQuery(String query) throws SQLException, ClassNotFoundException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        try{
            statement.executeUpdate(query);
        }
        catch (Exception e)
        {
            System.out.println("Here?"+e);
        }
        DbUtils.disconnectFromDb(connection, statement);
    }
    //delete this
    public static String tryGetting(String query) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        ResultSet res = statement.executeQuery(query);
        res.next();
        String out = res.getString("name");
        DbUtils.disconnectFromDb(connection, statement);
        return out;
    }

    public static void disconnectFromDb(Connection conn, Statement statement) {
        try
        {
           if(conn != null && statement != null)
           {
               conn.close();
               statement.close();
           }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
