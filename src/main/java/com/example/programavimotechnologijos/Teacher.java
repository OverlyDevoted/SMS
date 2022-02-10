package com.example.programavimotechnologijos;

import com.example.constants.Admin;
import com.example.control.DbUtils;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;

public class Teacher extends User implements Serializable {

    String qualification;
    Currency salary;
    float workHours;

    public Teacher(String name, String surname, String login, String password, int id_postpix, String qualification) {
        super(name, surname, login, password, id_postpix);
        this.qualification = qualification;
        this.salary = salary;
        this.workHours = workHours;
    }

    public Teacher() {
    }

    public Teacher(int id, String name, String surname, String password, String qualification) {
        super(id, name, surname, password);
        this.qualification = qualification;
    }

    public static Teacher getTeacherById(int id) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM teachers WHERE id = " + id;

        Teacher teacher = new Teacher();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            teacher = new Teacher(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5));
        }
        catch (Exception e)
        {
            System.out.println(e);
            teacher = null;
        }
        DbUtils.disconnectFromDb(connection, statement);
        return teacher;
    }
    public static void createTeacher(Teacher teacher) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        String Insert = "INSERT INTO teachers(id, name, surname, password, qualification) VALUES (?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(Insert);

        preparedStatement.setInt(1, teacher.getId());
        preparedStatement.setString(2, teacher.getName());
        preparedStatement.setString(3, teacher.getSurname());
        preparedStatement.setString(4, teacher.getPassword());
        preparedStatement.setString(5, teacher.getQualification());

        preparedStatement.execute();
        DbUtils.disconnectFromDb(connection, preparedStatement);
    }
    public static void updateTeacher(Teacher teacher) throws SQLException, ClassNotFoundException {
        if(teacher.getName() != "")
            DbUtils.tryQuery("UPDATE teachers SET name = '"+teacher.getName()+"' WHERE id = '" + teacher.getId() + "'");
        if(teacher.getSurname() != "")
            DbUtils.tryQuery("UPDATE teachers SET surname = '"+teacher.getSurname()+"' WHERE id = '" + teacher.getId() + "'");
        if(teacher.getPassword() != "")
            DbUtils.tryQuery("UPDATE teachers SET password = '"+teacher.getPassword()+"' WHERE id = '" + teacher.getId() + "'");
        if(teacher.getQualification() != "")
            DbUtils.tryQuery("UPDATE teachers SET qualification = '"+teacher.getQualification()+"' WHERE id = '" + teacher.getId() + "'");
    }
    public static void deleteTeacher(Teacher teacher) throws SQLException, ClassNotFoundException {
        DbUtils.tryQuery("DELETE FROM teachers WHERE id = '"+ teacher.getId() + "'");
    }
    public static ArrayList<Teacher> getTeachers() throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM teachers WHERE id != " + Admin.ADMIN_ID;
        ArrayList<Teacher> list = new ArrayList<Teacher>();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                Teacher teacher = new Teacher(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
                teacher.setId(rs.getInt(1));
                list.add(teacher);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        DbUtils.disconnectFromDb(connection, statement);
        return list;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Override
    public String toString() {
        return
                "{\"id\":\"" + getId() + '\"' +
                ", \"name\":\"" + getName() + '\"' +
                ", \"surname\":\"" + getSurname() + '\"' +
                ", \"qualification\":\"" + qualification + '\"' +
                        "}";

    }
}
