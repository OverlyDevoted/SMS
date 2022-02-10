package com.example.programavimotechnologijos;

import com.example.control.DbUtils;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Group implements Serializable {
    String name;
    int id;
    int course_id;
    int curator_id;
    int creator_id;

    public Group() {
    }
    public Group(int id, String name, int course_id, int curator_id, int creator_id) {
        this.name = name;
        this.id = id;
        this.course_id = course_id;
        this.curator_id = curator_id;
        this.creator_id = creator_id;
    }
    public Group(String name, int course_id, int curator_id, int creator_id) {
        this.name = name;
        this.course_id = course_id;
        this.curator_id = curator_id;
        this.creator_id = creator_id;
    }

    public static void createGroup(Group group) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        String Insert = "INSERT INTO groupings(name, course_id, curator, creator_id) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(Insert);
        System.out.println(group);
        preparedStatement.setString(1, group.getName());
        preparedStatement.setInt(2, group.getCourse_id());
        preparedStatement.setInt(3, group.getCurator_id());
        preparedStatement.setInt(4, group.getCreator_id());

        preparedStatement.execute();
        DbUtils.disconnectFromDb(connection, preparedStatement);
    }
    public static Group getGroupByID(int id) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM groupings WHERE id = " + id;

        Group group = new Group();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            group = new Group(rs.getInt(1), rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5));
        }
        catch (Exception e)
        {
            System.out.println(e);
            group = null;
        }
        DbUtils.disconnectFromDb(connection, statement);
        return group;
    }
    public static ArrayList<Group> getGroupByCreatorAndCurator(int creator_id, int curator) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM groupings WHERE creator_id = " + creator_id + " or curator = " + curator;

        ArrayList<Group> list = new ArrayList<Group>();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                Group group = new Group(rs.getInt(1), rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5));
                list.add(group);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        DbUtils.disconnectFromDb(connection, statement);
        return list;
    }
    public static ArrayList<Group> getGroupByCreator(int creator_id) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM groupings WHERE creator_id = " + creator_id;

        ArrayList<Group> list = new ArrayList<Group>();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                Group group = new Group(rs.getInt(1), rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5));
                list.add(group);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        DbUtils.disconnectFromDb(connection, statement);
        return list;
    }
    public static void updateGroup(Group group) throws SQLException, ClassNotFoundException {
        if(group.getName() != "")
            DbUtils.tryQuery("UPDATE groupings SET name = '"+group.getName()+"' WHERE id = '" + group.getId() + "'");
        if(group.getCourse_id() != 0)
            DbUtils.tryQuery("UPDATE groupings SET course_id = '"+group.getCourse_id()+"' WHERE id = '" + group.getId() +"'");
        if(group.getCurator_id() != 0)
            DbUtils.tryQuery("UPDATE groupings SET curator = '"+group.getCurator_id()+"' WHERE id = '" + group.getId() +"'");

    }
    public static void deleteGroup(Group group) throws SQLException, ClassNotFoundException {
        DbUtils.tryQuery("DELETE FROM groupings WHERE id = '"+ group.getId() + "'");
    }
    public static ArrayList<Group> getAllGroups() throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM groupings";
        ArrayList<Group> list = new ArrayList<Group>();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                Group group = new Group(rs.getInt(1), rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5));
                list.add(group);
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
                ", \"course_id\":\"" + course_id + '\"' +
                ", \"curator\":\"" + curator_id + '\"' +
                ", \"creator_id\":\"" + creator_id +
                "\"}";
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public int getCurator_id() {
        return curator_id;
    }
    public void setCurator_id(int curator_id) {
        this.curator_id = curator_id;
    }
    public int getCreator_id() {
        return creator_id;
    }
    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }
}
