package com.example.programavimotechnologijos;

import com.example.control.DbUtils;

import java.sql.*;
import java.util.ArrayList;

public class Course {

    int id;
    String name;
    String description;
    int creator_id;

    public Course(int id, String name, String description, int creator_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator_id = creator_id;
    }

    public Course() {}
    public static void createCourse(Course course) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        String Insert = "INSERT INTO course(name, description, creator_id) VALUES (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(Insert);
        preparedStatement.setString(1, course.getName());
        preparedStatement.setString(2, course.getDescription());
        preparedStatement.setInt(3, course.getCreator_id());
        preparedStatement.execute();
        DbUtils.disconnectFromDb(connection, preparedStatement);
    }
    public static Course getCourseByID(int id) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM course WHERE id = " + id;

        Course course = new Course();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            course = new Course(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getInt(4));
        }
        catch (Exception e)
        {
            System.out.println(e);
            course = null;
        }
        DbUtils.disconnectFromDb(connection, statement);
        return course;
    }
    public static Course getCourseByStudent(int student_id) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();

        String query = "select course.id, course.name, course.description, course.creator_id from groupings, course where groupings.course_id = course.id and groupings.id in (select group_id from students where id = "+ student_id+")";
        Course course = new Course();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            course = new Course(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getInt(4));

        }
        catch (Exception e)
        {
            System.out.println(e);
            course = null;
        }
        DbUtils.disconnectFromDb(connection, statement);
        return course;
    }
    public static void updateCourse(Course course) throws SQLException, ClassNotFoundException {
        if(course.getName()!= "")
            DbUtils.tryQuery("UPDATE course SET name = '"+course.getName()+"' WHERE id = '" + course.getId() + "'");
        if(course.getDescription()!="")
            DbUtils.tryQuery("UPDATE course SET description = '"+course.getDescription()+"' WHERE id = '" + course.getId() +"'");

    }
    public static void deleteCourse(Course course) throws SQLException, ClassNotFoundException {
        DbUtils.tryQuery("DELETE FROM course WHERE id = '"+ course.getId() + "'");
    }
    public static ArrayList<Course> getCourses() throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM course";
        ArrayList<Course> list = new ArrayList<Course>();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                Course course = new Course(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getInt(4));
                list.add(course);
            }
        }
        catch (Exception e)
        {
            System.out.println("Get all courses: " + e);
        }

        DbUtils.disconnectFromDb(connection, statement);
        return list;
    }
    public static ArrayList<Course> getCoursesByCreatorID(int creator_id) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM course WHERE creator_id = " + creator_id;
        ArrayList<Course> list = new ArrayList<Course>();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                Course course = new Course(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getInt(4));
                list.add(course);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        DbUtils.disconnectFromDb(connection, statement);
        return list;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id +
                ", \"name\":\"" + name + '\"' +
                ", \"description\":\"" + description + '\"' +
                ", \"creator_id\":\"" + creator_id +
                "\"}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }
}
