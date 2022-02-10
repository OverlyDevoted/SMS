package com.example.programavimotechnologijos;

import com.example.constants.Admin;
import com.example.control.DbUtils;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Subject implements Serializable {
    int id;
    String name;
    int credits;
    int creator_id;
    String description;
    int parent_subject_id;

    public Subject(int id, int credits, String name, int creator_id, String description) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.credits = credits;
        this.creator_id = creator_id;
    }
    public Subject(int credits, String name, int creator_id, String description) {

        this.description = description;
        this.name = name;
        this.credits = credits;
        this.creator_id = creator_id;
        this.parent_subject_id=-1;
    }
    public Subject(int credits, String name, int creator_id, String description, int parent_subject_id) {

        this.description = description;
        this.name = name;
        this.credits = credits;
        this.creator_id = creator_id;
        this.parent_subject_id = parent_subject_id;
    }
    public Subject(int id, int credits, String name, int creator_id, String description, int parent_subject_id) {
        this.id = id;
        this.credits = credits;
        this.name = name;
        this.creator_id = creator_id;
        this.description = description;
        this.parent_subject_id = parent_subject_id;
    }
    public Subject() {}

    @Override
    public String toString() {
        return "{\"id\":" + id +
                ", \"credits\":\"" + credits + '\"' +
                ", \"name\":\"" + name + '\"' +
                ", \"creator_id\":\"" + creator_id + '\"' +
                ", \"description\":\"" + description+ '\"' +
                ", \"parent_subject_id\":\"" + parent_subject_id +
                "\"}";
    }

    public static ArrayList<Subject> getSubjectByParent(int id) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "";
        if(id !=0)
        {
            query = "SELECT * FROM subjects WHERE parent_subject_id = " + id;
        }
        else
        {
            query =  "SELECT * FROM subjects WHERE parent_subject_id is null";
        }
        ArrayList<Subject> list = new ArrayList<Subject>();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                Subject subject = new Subject(rs.getInt(1), rs.getInt(2),rs.getString(3),rs.getInt(4),rs.getString(5),rs.getInt(6));
                list.add(subject);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        DbUtils.disconnectFromDb(connection, statement);
        return list;
    }
    public static Subject getSubjectById(int id) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM subjects WHERE id = " + id;

        Subject subject = new Subject();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            subject = new Subject(rs.getInt(1), rs.getInt(2),rs.getString(3),rs.getInt(4), rs.getString(5));
        }
        catch (Exception e)
        {
            System.out.println(e);
            subject = null;
        }
        DbUtils.disconnectFromDb(connection, statement);
        return subject;
    }
    public static void createSubject(Subject subject) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        String insert = new String();
        if(subject.parent_subject_id==-1)
        {
            insert = "INSERT INTO subjects(credits, name, creator_id, description) VALUES (?,?,?,?)";
        }
        else
        {
            insert = "INSERT INTO subjects(credits, name, creator_id, description, parent_subject_id) VALUES (?,?,?,?,?)";
        }
        PreparedStatement preparedStatement = connection.prepareStatement(insert);

        preparedStatement.setInt(1, subject.getCredits());
        preparedStatement.setString(2, subject.getName());
        preparedStatement.setInt(3, subject.getCreator_id());
        preparedStatement.setString(4, subject.getDescription());
        if(subject.parent_subject_id!=-1)
        preparedStatement.setInt(5, subject.getParent_subject_id());

        preparedStatement.execute();
        DbUtils.disconnectFromDb(connection, preparedStatement);
    }
    public static void updateSubject(Subject subject) throws SQLException, ClassNotFoundException {
        if(subject.getName() != "")
            DbUtils.tryQuery("UPDATE subjects SET name = '"+subject.getName()+"', credits = '"+ subject.getId() +"', creator_id ='"+subject.getCreator_id()+"', description = '"+subject.getDescription()+"'  WHERE id = '" + subject.getId() + "'");
        }
    public static void updateSubjectParent(Subject subject) throws SQLException, ClassNotFoundException {
        if(subject.getId() == subject.getParent_subject_id())
        {
            System.out.println("Error");
        }
        else
        {
            if(subject.getParent_subject_id()==0)
            {
                DbUtils.tryQuery("UPDATE subjects SET parent_subject_id = NULL WHERE id = " + subject.getId());
            }
            else
            {
                DbUtils.tryQuery("UPDATE subjects SET parent_subject_id = "+ subject.getParent_subject_id() + " WHERE id != " + subject.getParent_subject_id() + " and id = " + subject.getId());
            }
        }
    }
    public static void deleteSubject(Subject subject) throws SQLException, ClassNotFoundException {
        DbUtils.tryQuery("DELETE FROM subjects WHERE id = '"+ subject.getId() + "'");
    }
    public static ArrayList<Subject> getSubject() throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM subjects";
        ArrayList<Subject> list = new ArrayList<Subject>();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                Subject subject = new Subject(rs.getInt(1), rs.getInt(2),rs.getString(3),rs.getInt(4),rs.getString(5));
                list.add(subject);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        DbUtils.disconnectFromDb(connection, statement);
        return list;
    }
    public static ArrayList<Subject> getSubjectByCreatorID(int creator_id) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM subjects where creator_id = " + creator_id;
        ArrayList<Subject> list = new ArrayList<Subject>();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                Subject subject = new Subject(rs.getInt(1), rs.getInt(2),rs.getString(3),rs.getInt(4),rs.getString(5));
                list.add(subject);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        DbUtils.disconnectFromDb(connection, statement);
        return list;
    }
    public static ArrayList<Subject> getSubjectsInCourseByCreatorID(int creator_id, int course_id) throws SQLException, ClassNotFoundException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = new String();
        if(creator_id== Admin.ADMIN_ID)
        {
            query = "SELECT subjects.id, subjects.credits, subjects.name, subjects.creator_id, subjects.description FROM subjects WHERE subjects.id NOT IN (SELECT subjects_in_courses.subject_id FROM subjects_in_courses WHERE subjects_in_courses.course_id = " + course_id + ") and parent_subject_id is null";
        }
        else
        {
            query = "SELECT subjects.id, subjects.credits, subjects.name, subjects.creator_id, subjects.description FROM subjects WHERE subjects.id NOT IN (SELECT subjects_in_courses.subject_id FROM subjects_in_courses WHERE subjects_in_courses.course_id = " + course_id + ") AND subjects.creator_id = " + creator_id + " and parent_subject_id is null";
        }
        ArrayList<Subject> list = new ArrayList<>();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                Subject subject = new Subject(rs.getInt(1), rs.getInt(2),rs.getString(3),rs.getInt(4),rs.getString(5));
                list.add(subject);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        DbUtils.disconnectFromDb(connection, statement);
        return list;
    }
    public static ArrayList<Subject> getSubjectsNotInCourseByCreatorID(int creator_id, int course_id) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = new String();
        if(creator_id==Admin.ADMIN_ID)
        {
            query = "SELECT subjects.id, subjects.credits, subjects.name, subjects.creator_id, subjects.description FROM subjects WHERE subjects.id IN (SELECT subjects_in_courses.subject_id FROM subjects_in_courses WHERE subjects_in_courses.course_id = " + course_id + ") AND parent_subject_id is null";
        }
        else
        {
            query = "SELECT subjects.id, subjects.credits, subjects.name, subjects.creator_id, subjects.description FROM subjects WHERE subjects.id IN (SELECT subjects_in_courses.subject_id FROM subjects_in_courses WHERE subjects_in_courses.course_id = " + course_id + ") AND subjects.creator_id = " + creator_id + " and parent_subject_id is null";

        }
        ArrayList<Subject> list = new ArrayList<>();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                Subject subject = new Subject(rs.getInt(1), rs.getInt(2),rs.getString(3),rs.getInt(4),rs.getString(5));
                list.add(subject);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        DbUtils.disconnectFromDb(connection, statement);
        return list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public int getParent_subject_id() {
        return parent_subject_id;
    }
    public void setParent_subject_id(int parent_subject_id) {
        this.parent_subject_id = parent_subject_id;
    }
}
